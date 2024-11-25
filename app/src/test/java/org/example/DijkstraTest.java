package org.example;

import static org.junit.Assert.assertEquals;

import org.Main.Dijkstra;
import org.Main.Graph;
import org.Main.QueryResult;
import org.junit.Test;

public class DijkstraTest {
    @Test
    public void testSingleEdge() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addVertex(1, 1, 1);
        graph.addEdge(0, 1, 5);

        QueryResult result = Dijkstra.dijkstra(graph, 0, 1);
        assertEquals(5, result.getShortestPath());
        assertEquals(1, result.getRelaxedEdges());
    }

    @Test
    public void testMultipleEdges() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addVertex(1, 1, 1);
        graph.addVertex(2, 2, 2);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 4);
        graph.addEdge(0, 2, 10);

        QueryResult result = Dijkstra.dijkstra(graph, 0, 2);
        assertEquals(7, result.getShortestPath());
        assertEquals(3, result.getRelaxedEdges());
    }

    @Test
    public void testDisconnectedGraph() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addVertex(1, 1, 1);
        graph.addVertex(2, 2, 2);
        graph.addEdge(0, 1, 5);

        QueryResult result = Dijkstra.dijkstra(graph, 0, 2);
        assertEquals(-1, result.getShortestPath());
        assertEquals(1, result.getRelaxedEdges());
    }

    @Test
    public void testSelfLoop() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addEdge(0, 0, 5);

        QueryResult result = Dijkstra.dijkstra(graph, 0, 0);
        assertEquals(0, result.getShortestPath());
        assertEquals(0, result.getRelaxedEdges());
    }

    @Test
    public void testComplexGraph() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addVertex(1, 1, 1);
        graph.addVertex(2, 2, 2);
        graph.addVertex(3, 3, 3);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 4);
        graph.addEdge(2, 3, 1);

        QueryResult result = Dijkstra.dijkstra(graph, 0, 3);
        assertEquals(4, result.getShortestPath());
        assertEquals(4, result.getRelaxedEdges());
    }

    @Test
    public void testNoEdges() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addVertex(1, 1, 1);

        QueryResult result = Dijkstra.dijkstra(graph, 0, 1);
        assertEquals(-1, result.getShortestPath());
        assertEquals(0, result.getRelaxedEdges());
    }

    @Test
    public void testDijkstraWithEarlyStopping() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addVertex(1, 1, 1);
        graph.addVertex(2, 2, 2);
        graph.addVertex(3, 3, 3);
        graph.addEdge(1, 2, 5);
        graph.addEdge(2, 3, 3);

        QueryResult result = Dijkstra.dijkstra(graph, 1, 3);
        assertEquals(8, result.getShortestPath()); // 1 -> 2 -> 3 with cost 5 + 3
        assertEquals(2, result.getRelaxedEdges()); // Two edges relaxed
    }

    @Test
    public void testDijkstraWithUnreachableTarget() {
        Graph graph = new Graph();
        graph.addVertex(0, 0, 0);
        graph.addVertex(1, 1, 1);
        graph.addVertex(2, 2, 2);
        graph.addVertex(3, 3, 3);
        graph.addEdge(1, 2, 5);

        QueryResult result = Dijkstra.dijkstra(graph, 1, 3);
        assertEquals(-1, result.getShortestPath()); // No path to node 3
        assertEquals(1, result.getRelaxedEdges()); // Only one edge processed
    }
}
