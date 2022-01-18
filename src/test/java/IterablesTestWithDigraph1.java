import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterablesTestWithDigraph1 {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        List<Integer> sources = new ArrayList<>(Arrays.asList(1, 7));
        List<Integer> destinations = new ArrayList<>(Arrays.asList(0, 1));
        int result = sap.length(sources, destinations);
        if (result != 0)
            throw new AssertionError("The minimum distances of two iterable sets should be 0, but it " +
                    "is: " + result);
        In in = new In("digraph25.txt");
        digraph = new Digraph(in);
        sap = new SAP(digraph);
        sources = new ArrayList<>(Arrays.asList(13, 23, 24));
        destinations = new ArrayList<>(Arrays.asList(6, 16, 17));
        result = sap.length(sources, destinations);
        if (result != 4)
            throw new AssertionError("The shortest ancestral path of digraph25 example should be 4, but " +
                    "it is: " + result);
    }
}
