package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.Main.Edge;
import org.Main.Graph;
import org.Main.Vertex;
import org.junit.Test;

public class BuildGraphTest {

    @Test
    public void testAddVertex() {
        Graph graph = new Graph();
        graph.addVertex(1L, 10.0, 20.0);

        Vertex vertex = graph.getVertexById(1L);
        assertNotNull(vertex);
        assertEquals(1, vertex.getId());
        //assertEquals(10.0, vertex.getLongitude());
        //assertEquals(20.0, vertex.getLatitude());
    }

    @Test
    public void testAddEdge() {
        Graph graph = new Graph();
        graph.addVertex(1, 10.0, 20.0);
        graph.addVertex(2, 30.0, 40.0);
        graph.addEdge(1, 2, 50);

        List<Edge> edges = graph.getAdjacencyList().get(1L);
        assertNotNull(edges);
        assertEquals(1, edges.size());
        assertEquals(50, edges.get(0).getCost());
    }

    @Test
    public void testHasEdge() {
        Graph graph = new Graph();
        graph.addVertex(1, 10.0, 20.0);
        graph.addVertex(2, 30.0, 40.0);
        graph.addEdge(1, 2, 50);

        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1)); // Directed graph assumption
    }

    @Test
    public void testGetEdgeCost() {
        Graph graph = new Graph();
        graph.addVertex(1, 10.0, 20.0);
        graph.addVertex(2, 30.0, 40.0);
        graph.addEdge(1, 2, 50);

        assertEquals(50, graph.getEdgeCost(1, 2));
        assertEquals(Integer.MAX_VALUE, graph.getEdgeCost(2, 1)); // No reverse edge
    }

    @Test
    public void testRemoveVertex() {
        Graph graph = new Graph();
        graph.addVertex(1, 10.0, 20.0);
        graph.addVertex(2, 30.0, 40.0);
        graph.addEdge(1, 2, 50);

        graph.removeVertex(1L);

        assertNull(graph.getVertexById(1));
    }

    @Test
    public void testGetEdges() {
        Graph graph = new Graph();
        graph.addVertex(1L, 10.0, 20.0);
        graph.addVertex(2L, 30.0, 40.0);
        graph.addEdge(1L, 2L, 50);

        List<Edge> edges = graph.getEdges();
        assertEquals(1, edges.size());
        assertEquals(50, edges.get(0).getCost());
    }

    @Test
    public void testReadGraphFromInput() throws Exception {
        String input = "5 6\n" +
                       "1 10.0 20.0\n" +
                       "2 15.0 25.0\n" +
                       "3 20.0 30.0\n" +
                       "4 25.0 35.0\n" +
                       "5 30.0 40.0\n" +
                       "1 2 10\n" +
                       "1 3 15\n" +
                       "2 4 20\n" +
                       "3 4 25\n" +
                       "4 5 30\n" +
                       "5 1 35\n";
    
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Graph graph = Graph.readGraphFromInput(inputStream);
    
        // Check vertices
        Map<Long, Vertex> vertices = graph.getVertices();
        assertEquals(5, vertices.size());
        assertNotNull(vertices.get(1L));
        assertNotNull(vertices.get(2L));
        assertNotNull(vertices.get(3L));
        assertNotNull(vertices.get(4L));
        assertNotNull(vertices.get(5L));
    
        // Check edges
        assertTrue(graph.hasEdge(1L, 2L));
        assertTrue(graph.hasEdge(1L, 3L));
        assertTrue(graph.hasEdge(2L, 4L));
        assertTrue(graph.hasEdge(3L, 4L));
        assertTrue(graph.hasEdge(4L, 5L));
        assertTrue(graph.hasEdge(5L, 1L));
    
        // Check edge costs
        assertEquals(10, graph.getEdgeCost(1L, 2L));
        assertEquals(15, graph.getEdgeCost(1L, 3L));
        assertEquals(20, graph.getEdgeCost(2L, 4L));
        assertEquals(25, graph.getEdgeCost(3L, 4L));
        assertEquals(30, graph.getEdgeCost(4L, 5L));
        assertEquals(35, graph.getEdgeCost(5L, 1L));
    }
    
}