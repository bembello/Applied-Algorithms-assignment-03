package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.Main.ContractionHierarchy;
import org.Main.Graph;
import org.Main.Vertex;
import org.junit.Before;
import org.junit.Test;

public class ContractionHierarchyTest {

    private Graph graph;
    private ContractionHierarchy contractionHierarchy;

    @Before
    public void setUp() {
        // Setup a basic graph with vertices and edges for testing
        graph = new Graph();
        Vertex v1 = new Vertex(1, 10, 20);
        Vertex v2 = new Vertex(2, 20, 30);
        Vertex v3 = new Vertex(3, 30, 40);
        Vertex v4 = new Vertex(4, 40, 50);

        // Add vertices
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        // Add edges
        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 20);
        graph.addEdge(2, 4, 30);
        graph.addEdge(3, 4, 40);

        // Initialize ContractionHierarchy with this graph
        contractionHierarchy = new ContractionHierarchy(graph);
    }

    @Test
    public void testPreprocess() {
        // Test the preprocessing step
        contractionHierarchy.preprocess();
        assertEquals(4, contractionHierarchy.getVertexOrder().size());
        assertTrue(contractionHierarchy.getTotalShortcutsAdded() >= 0);
    }

    @Test
    public void testContractVertex() {
        // Test contraction of a single vertex
        Vertex v = graph.getVertexById(1); // Vertex with id 1
        int shortcutsBefore = contractionHierarchy.getTotalShortcutsAdded();
        int shortcutsAdded = contractionHierarchy.contractVertex(v);
        int shortcutsAfter = contractionHierarchy.getTotalShortcutsAdded();

        assertTrue(shortcutsAfter >= shortcutsBefore);
        assertNotNull(v);
    }

    @Test
    public void testGetAugmentedGraph() {
        // Test the augmented graph after preprocessing
        contractionHierarchy.preprocess();
        Graph augmentedGraph = contractionHierarchy.getAugmentedGraph();

        // Check if the augmented graph has the expected vertices and edges
        assertNotNull(augmentedGraph);
        assertTrue(augmentedGraph.getVertices().size() >= graph.getVertices().size());
    }

    @Test
    public void testExportAugmentedGraph() throws IOException {
        contractionHierarchy.preprocess();
        String filename = "augmented_graph.txt";
        contractionHierarchy.exportAugmentedGraph(filename);

        // Verify if the file exists after export
        File file = new File(filename);
        assertTrue(file.exists());

        // Clean up
        file.delete();
    }

    @Test
    public void testGetNodePriority() {
        // Test the calculation of node priority
        Vertex v = graph.getVertexById(1); // Vertex with id 1
        int priority = contractionHierarchy.getNodePriority(v);
        assertTrue(priority >= 0);
    }
}
