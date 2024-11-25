package org.Main;

import java.io.InputStream;
import java.util.List;

public class Main {

    // Implement the contraction phase using the ContractionHierarchy class
    private static ContractionHierarchy contractionPhase(Graph graph) {
        // Initialize the ContractionHierarchy with the given graph
        ContractionHierarchy contractionHierarchy = new ContractionHierarchy(graph);

        // Perform the preprocessing phase (i.e., contraction)
        long start = System.nanoTime();
        contractionHierarchy.preprocess();  // This method will contract nodes and add shortcut edges
        long end = System.nanoTime();
        double contractionTimeInSeconds = (end - start) / 1_000_000_000.0; // Convert to seconds
        System.out.println("Contraction time (s): " + contractionTimeInSeconds);

        // Export the augmented graph with shortcut edges
        contractionHierarchy.exportAugmentedGraph("augmented_denmark.graph");
        
        return contractionHierarchy; // Return the ContractionHierarchy for further usage
    }

    public static void main(String[] args) {
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("denmark.graph");
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found in resources: denmark.graph");
            }

            Graph graph = Graph.readGraphFromInput(inputStream);

            // Generate random pairs 
            int numVertices = graph.getVertices().size();
            List<int[]> pairs = RandomPairs.generateRandomPairs(1000, numVertices, 314159);
            int queryCount = pairs.size();

            // Perform the contraction phase (preprocessing)
            ContractionHierarchy contractionHierarchy = contractionPhase(graph);

            // Use the augmented graph (with shortcuts) for bidirectional Dijkstra
            Graph augmentedGraph = contractionHierarchy.getAugmentedGraph();

            // Compare performance of algorithms
System.out.println("Comparing performance of algorithms...");

// Unoptimized Dijkstra
System.out.println("Unoptimized Dijkstra:");
long unoptimizedTotalQueryTime = 0;
long unoptimizedTotalRelaxedEdges = 0;

for (int[] pair : pairs) {
    int source = pair[0];
    int target = pair[1];

    long start = System.nanoTime();
    QueryResult result = Dijkstra.dijkstra(graph, source, target);
    long end = System.nanoTime();

    unoptimizedTotalQueryTime += (end - start);
    unoptimizedTotalRelaxedEdges += result.getRelaxedEdges();
}

    double unoptimizedAvgTime = unoptimizedTotalQueryTime / 1_000_000.0 / queryCount;
    double unoptimizedAvgRelaxedEdges = unoptimizedTotalRelaxedEdges / (double) queryCount;

    System.out.println("Average query time (ms): " + unoptimizedAvgTime);
    System.out.println("Average number of relaxed edges: " + unoptimizedAvgRelaxedEdges);

    // Bidirectional Dijkstra
    System.out.println("Bidirectional Dijkstra:");
    long bidirectionalTotalQueryTime = 0;
    long bidirectionalTotalRelaxedEdges = 0;

    for (int[] pair : pairs) {
        int source = pair[0];
        int target = pair[1];

        long start = System.nanoTime();
        QueryResult result = BidirectionalDijkstra.bidirectionalDijkstra(graph , source, target);
        long end = System.nanoTime();

        bidirectionalTotalQueryTime += (end - start);
        bidirectionalTotalRelaxedEdges += result.getRelaxedEdges();
    }

        double bidirectionalAvgTime = bidirectionalTotalQueryTime / 1_000_000.0 / queryCount;
        double bidirectionalAvgRelaxedEdges = bidirectionalTotalRelaxedEdges / (double) queryCount;

        System.out.println("Average query time (ms): " + bidirectionalAvgTime);
        System.out.println("Average number of relaxed edges: " + bidirectionalAvgRelaxedEdges);


        System.out.println("Bidirectional CH Dijkstra:");
        long bidirectionalCHTotalQueryTime = 0;
        long bidirectionalCHTotalRelaxedEdges = 0;

        for (int[] pair : pairs) {
            int source = pair[0];
            int target = pair[1];

            long start = System.nanoTime();
            QueryResult result = BidirectionalDijkstraCH.bidirectionalDijkstra(augmentedGraph, source, target);
            long end = System.nanoTime();

            bidirectionalCHTotalQueryTime += (end - start);
            bidirectionalCHTotalRelaxedEdges += result.getRelaxedEdges();
        }

        double bidirectionalCHAvgTime = bidirectionalCHTotalQueryTime / 1_000_000.0 / queryCount;
        double bidirectionalCHAvgRelaxedEdges = bidirectionalCHTotalRelaxedEdges / (double) queryCount;

        System.out.println("Average query time (ms): " + bidirectionalCHAvgTime);
        System.out.println("Average number of relaxed edges: " + bidirectionalCHAvgRelaxedEdges);


        // Report comparative results
        System.out.println("Performance comparison:");
        System.out.println("Unoptimized Dijkstra vs. Bidirectional Dijkstra vs. Bidirectional CH Dijkstra:");
        System.out.println("Average query time (ms): " + unoptimizedAvgTime + " vs. " + bidirectionalAvgTime + " vs. " + bidirectionalCHAvgTime);
        System.out.println("Average number of relaxed edges: " + unoptimizedAvgRelaxedEdges + " vs. " + bidirectionalAvgRelaxedEdges + " vs. " + bidirectionalCHAvgRelaxedEdges);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
