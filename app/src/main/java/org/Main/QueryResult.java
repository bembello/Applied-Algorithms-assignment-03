package org.Main;

public class QueryResult {
    private long shortestPath;
    private long relaxedEdges;

    public QueryResult(long shortestPath, long relaxedEdges) {
        this.shortestPath = shortestPath;
        this.relaxedEdges = relaxedEdges;
    }

    public long getShortestPath() {
        return shortestPath;
    }

    public long getRelaxedEdges() {
        return relaxedEdges;
    }
}
