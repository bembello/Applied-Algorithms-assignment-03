package org.Main;

import java.util.HashSet;
import java.util.Set;

public class Vertex {
    private long id;  // Change from int to long
    private double longitude;
    private double latitude;
    private Set<Edge> edges;

    // Constructor now includes longitude and latitude
    public Vertex(long id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.edges = new HashSet<>();
    }

    // Getters
    public long getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    // Add an edge to the vertex
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    // Get the cost of the edge between this vertex and the given vertex 'v'
    public int getCostTo(Vertex v) {
        for (Edge edge : edges) {
            // Check if this edge connects to the vertex 'v'
            if (edge.getTo() == v.getId()) {  // Compare long id values
                return edge.getCost();
            }
        }
        // If no edge connects to 'v', return -1 or some other indication that no path exists
        return -1;  // Or throw an exception, depending on your design
    }

    // Get active (non-contracted) edges
    public Set<Edge> getActiveEdges() {
        Set<Edge> activeEdges = new HashSet<>();
        for (Edge edge : edges) {
            if (!edge.isContracted()) {  // Only include edges that are not contracted
                activeEdges.add(edge);
            }
        }
        return activeEdges;
    }
}
