package org.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPairs {
    public static List<int[]> generateRandomPairs(int numPairs, int numVertices, long seed) {
        List<int[]> pairs = new ArrayList<>();
        Random random = new Random(seed);

        for (int i = 0; i < numPairs; i++) {
            int s = random.nextInt(numVertices); // Generates a value between 0 and numVertices - 1
            int t = random.nextInt(numVertices); // Generates a value between 0 and numVertices - 1
            pairs.add(new int[]{s, t});
        }
        System.out.println("Generating random pairs: done");
        return pairs;
    }
}
