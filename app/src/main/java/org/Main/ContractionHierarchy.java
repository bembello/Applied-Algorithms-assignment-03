package org.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ContractionHierarchy {
    private Graph graph;
    private List<Vertex> vertexOrder;
    private Set<Vertex> contractedVertices;
    private Map<Vertex, Integer> rankMap;
    private List<Edge> allEdges;
    private int updateThreshold = 50;
    private int totalShortcutsAdded = 0;

    public ContractionHierarchy(Graph graph) {
        this.graph = graph;
        this.vertexOrder = new ArrayList<>();
        this.contractedVertices = new HashSet<>();
        this.rankMap = new HashMap<>();
        this.allEdges = new ArrayList<>();
    }

    public void preprocess() {
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(v -> this.getNodePriority(v)));

        Set<Vertex> dirtyVertices = new HashSet<>();
        int updateCount = 0;

        // Initialize priority queue
        for (Vertex v : graph.getVertices().values()) {
            if (!contractedVertices.contains(v)) {
                priorityQueue.add(v);
            }
        }

        System.out.println("Preprocessing started. Total vertices: " + graph.getVertices().size());
        System.out.println("Preprocessing started. Total edges: " + graph.getEdges().size());

        while (!priorityQueue.isEmpty() && contractedVertices.size() < graph.getVertices().size()) {
            Vertex v = priorityQueue.poll();

            // Skip already-contracted vertices
            if (contractedVertices.contains(v)) continue;

            // Add vertex to contraction order and mark as contracted
            vertexOrder.add(v);
            contractedVertices.add(v);
            rankMap.put(v, vertexOrder.size());

            int shortcutsAdded = contractVertex(v);
            totalShortcutsAdded += shortcutsAdded;

            // Update neighbors
            for (Edge e : v.getEdges()) {
                Vertex neighbor = graph.getVertexById(e.getTo());
                if (!contractedVertices.contains(neighbor)) {
                    dirtyVertices.add(neighbor);
                }
            }

            // Lazy updates
            updateCount++;
            if (updateCount >= updateThreshold) {
                for (Vertex dirtyVertex : dirtyVertices) {
                    if (!contractedVertices.contains(dirtyVertex)) {
                        priorityQueue.remove(dirtyVertex);
                        priorityQueue.add(dirtyVertex);
                    }
                }
                updateCount = 0;
                dirtyVertices.clear();
            }
        }

        System.out.println("Preprocessing complete. Total shortcuts added: " + totalShortcutsAdded);
    }

    public int getNodePriority(Vertex v) {
        int edgeDiff = getEdgeDifference(v);
        int deletedNeighbors = getDeletedNeighbors(v);
        return (int) (edgeDiff + 0.75 * deletedNeighbors);
    }

    private int getEdgeDifference(Vertex v) {
        Set<Edge> activeEdges = v.getActiveEdges();
        int contractedNeighbors = (int) v.getEdges().stream()
                .filter(e -> contractedVertices.contains(graph.getVertexById(e.getTo())))
                .count();
        return activeEdges.size() + contractedNeighbors;
    }

    private int getDeletedNeighbors(Vertex v) {
        return (int) v.getEdges().stream()
                .filter(e -> contractedVertices.contains(graph.getVertexById(e.getTo())))
                .count();
    }

    
    public List<Vertex> getVertexOrder() {
        return vertexOrder;
    }

    public int getTotalShortcutsAdded() {
        return totalShortcutsAdded;
    }

    
    public Graph getAugmentedGraph() {
        Graph augmentedGraph = new Graph();

        // Add all vertices from the original graph
        for (Vertex vertex : graph.getVertices().values()) {
            augmentedGraph.addVertex(vertex);
        }

        // Add edges from the original graph that are not contracted
        for (Edge edge : graph.getEdges()) {
            if (!contractedVertices.contains(graph.getVertexById(edge.getTo()))) {
                augmentedGraph.addEdge(edge.getFrom(), edge.getTo(), edge.getCost());
            }
        }

        // Add shortcut edges
        for (Edge shortcutEdge : allEdges) {
            augmentedGraph.addEdge(shortcutEdge.getFrom(), shortcutEdge.getTo(), shortcutEdge.getCost());
            augmentedGraph.addEdge(shortcutEdge.getTo(), shortcutEdge.getFrom(), shortcutEdge.getCost()); // Ensure bidirectional shortcuts
        }

        return augmentedGraph;
    }
    private Set<String> shortcutSet = new HashSet<>();

    public int contractVertex(Vertex v) {
        int shortcutsAdded = 0;
    
        Map<Vertex, Integer> neighbors = new HashMap<>();
        for (Edge edge : v.getEdges()) {
            Vertex neighbor = graph.getVertexById(edge.getTo());
            if (!contractedVertices.contains(neighbor)) {
                neighbors.put(neighbor, edge.getCost());
            }
        }
    
        for (Vertex u : neighbors.keySet()) {
            for (Vertex w : neighbors.keySet()) {
                if (!u.equals(w)) {
                    int shortcutCost = neighbors.get(u) + neighbors.get(w);
    
                    // Create a unique identifier for the shortcut
                    String shortcutId = Math.min(u.getId(), w.getId()) + ":" + Math.max(u.getId(), w.getId());
    
                    // Check if shortcut already exists
                    if (!shortcutSet.contains(shortcutId)) {
                        boolean hasEdge = graph.hasEdge(u.getId(), w.getId());
                        int existingEdgeCost = hasEdge ? graph.getEdgeCost(u.getId(), w.getId()) : -1;
    
                        if (!hasEdge || existingEdgeCost > shortcutCost) {
                            graph.addEdge(u.getId(), w.getId(), shortcutCost);
                            allEdges.add(new Edge(u.getId(), w.getId(), shortcutCost));
                            shortcutSet.add(shortcutId); // Mark this shortcut as added
                            shortcutsAdded++;
                        }
                    }
                }
            }
        }
    
        return shortcutsAdded;
    }

    public void exportAugmentedGraph(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write the number of vertices and edges (original + shortcuts)
            writer.write(graph.getVertices().size() + " " + (graph.getEdges().size() + allEdges.size()));
            writer.newLine();
    
            // Write vertex IDs and their ranks
            for (Vertex vertex : graph.getVertices().values()) {
                int rank = rankMap.getOrDefault(vertex, -1); // Default rank for uncontracted vertices
                writer.write(vertex.getId() + " " + rank);
                writer.newLine();
            }
    
            // Write original edges with -1 as the contracted node identifier
            for (Edge edge : graph.getEdges()) {
                if (!contractedVertices.contains(graph.getVertexById(edge.getTo()))) {
                    writer.write(edge.getFrom() + " " + edge.getTo() + " " + edge.getCost() + " -1");
                    writer.newLine();
                }
            }
    
            // Write shortcut edges with the contracted node identifier
            for (Edge shortcutEdge : allEdges) {
                writer.write(shortcutEdge.getFrom() + " " + shortcutEdge.getTo() + " " 
                            + shortcutEdge.getCost() + " " + shortcutEdge.getFrom());
                writer.newLine();
            }
    
            System.out.println("Exported augmented graph to " + filename);
            System.out.println("Output file length (lines): " 
                               + (graph.getVertices().size() + graph.getEdges().size() + allEdges.size()));
    
        } catch (IOException e) {
            System.err.println("Error writing augmented graph to file: " + e.getMessage());
        }
    }
    
}
