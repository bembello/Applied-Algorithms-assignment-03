package org.Main;

public class Edge {
    private long from;
    private long to;
    private int cost;
    private boolean contracted;  // Track whether this edge is contracted

    // Constructor
    public Edge(long from, long to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.contracted = false;  // Initially not contracted
    }

    // Getters
    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public int getCost() {
        return cost;
    }

    // Check if the edge is contracted
    public boolean isContracted() {
        return contracted;
    }

    // Set the contracted status of the edge
    public void setContracted(boolean contracted) {
        this.contracted = contracted;
    }
}
