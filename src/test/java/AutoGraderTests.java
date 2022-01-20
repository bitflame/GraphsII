import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class AutoGraderTests {
    public static void main(String[] args) {
        StdOut.println("----------------------------------Testing Digraph3----------------------------------");
        Digraph digraph = new Digraph(new In("digraph3.txt"));
        SAP sap = new SAP(digraph);
        int distance = sap.length(10, 7);
        if (distance != 3) throw new AssertionError("Distance between 10 and 7 should be 3, but it is: " + distance);
        distance = sap.length(5, 14);
        if (distance != -1)
            throw new AssertionError("Distance between 5 and 14 should be -1 since the two nodes are not connected, but it is: " + distance);
        distance = sap.length(2, 13);
        if (distance != -1)
            throw new AssertionError("Distance between 2 and 13 should be -1 since the two nodes are not connected, but it is: " + distance);
        distance = sap.length(10, 3);
        if (distance != -1)
            throw new AssertionError("Distance between 10 and 3 should be -1 since the two nodes are not connected, but it is: " + distance);
        int ancesotr = sap.getAncestor(7, 11);
        if (ancesotr != 8)
            throw new AssertionError("The value of ancestor between 7 and 11 should be 8, but it is: " + ancesotr);
        distance = sap.length(7, 11);
        if (distance != 3)
            throw new AssertionError("The distance between 7 and 11 should be 3, but it is: " + distance);
        distance = sap.length(2, 3);
        if (distance != 1) throw new AssertionError("The distance between 2 and 3 should be 1, but it is: " + distance);
        distance = sap.length(10, 9);
        if (distance != 1)
            throw new AssertionError("The distance between 10 and 9 should be 1, but it is: " + distance);
        distance = sap.length(12,13);
        if (distance!=4) throw new AssertionError("The distance between 12 and 13 should be 4, but it is: "+ distance);
    }
}
