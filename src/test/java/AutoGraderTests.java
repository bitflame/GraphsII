import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class AutoGraderTests {
    public static void main(String[] args) {
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 1 ----------------------------------");
        Digraph digraph = new Digraph(new In("digraph1.txt"));
        SAP sap = new SAP(digraph);
        int distance = sap.length(3, 3);
        if (distance != 0) throw new AssertionError("Distance between 3, and 3 should be 0, but it is: " + distance);
        int ancestor = sap.ancestor(3, 3);
        if (ancestor != 3)
            throw new AssertionError("The value of ancestor between 3 and 3 should be 3, but it is: " + ancestor);
        distance = sap.length(3, 8);
        if (distance != 1)
            throw new AssertionError("The value of length between 3 and 8 should be 1, but it is: " + distance);
        distance = sap.length(1, 1);
        if (distance != 0)
            throw new AssertionError("The value of length between 1 and 1 should be 0, but it is: " + distance);
        distance = sap.length(3, 11);
        if (distance != 4)
            throw new AssertionError("The value of length between 3 and 11 should be 4, but it is: " + distance);
        ancestor = sap.ancestor(3, 11);
        if (ancestor != 1)
            throw new AssertionError("The value of ancestor of 3 and 11 should be 1, but it is: " + ancestor);
        distance = sap.length(9, 12);
        if (distance != 3) throw new AssertionError("The value of length between the nodes 9 and 12 should be 3, " +
                "but it is: " + distance);
        ancestor = sap.ancestor(9, 12);
        if (ancestor != 5)
            throw new AssertionError("The value of ancestor of 9 and 12 should be 5, but it is: " + ancestor);
        distance = sap.length(7, 2);
        if (distance != 4) throw new AssertionError("The value of length between the nodes 7, and 2 should be 4, " +
                "but it is: " + distance);
        ancestor = sap.ancestor(7, 2);
        if (ancestor != 0)
            throw new AssertionError("The value of ancestor of 7 and 2 should be 0, but it is: " + ancestor);
        distance = sap.length(1, 6);
        if (distance != -1) throw new AssertionError("The value of length between 1 and 6 should be -1 since they " +
                "are not connected but it is: " + distance);
        ancestor = sap.ancestor(1, 6);
        if (ancestor != -1) throw new AssertionError("The value of ancestor between 1 and 6 should be -1 since " +
                "they are not connected, but it is: " + ancestor);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 2 ----------------------------------");
        digraph = new Digraph(new In("digraph2.txt"));
        sap = new SAP(digraph);
        distance = sap.length(4, 1);
        if (distance != 3) throw new AssertionError("Distance between 4, and 1 should be 3, but it is: " + distance);
        distance = sap.length(4, 0);
        if (distance != 2) throw new AssertionError("Distance between 4, and 0 should be 2, but it is: " + distance);
        ancestor = sap.ancestor(4, 0);
        if (ancestor != 0)
            throw new AssertionError("The value of ancestor between 4, and 0 should be 0, but it is: " + ancestor);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 3 ----------------------------------");
        digraph = new Digraph(new In("digraph3.txt"));
        sap = new SAP(digraph);
        distance = sap.length(10, 7);
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
        ancestor = sap.ancestor(7, 11);
        if (ancestor != 8)
            throw new AssertionError("The value of ancestor between 7 and 11 should be 8, but it is: " + ancestor);
        distance = sap.length(7, 11);
        if (distance != 3)
            throw new AssertionError("The distance between 7 and 11 should be 3, but it is: " + distance);
        distance = sap.length(2, 3);
        if (distance != 1) throw new AssertionError("The distance between 2 and 3 should be 1, but it is: " + distance);
        distance = sap.length(10, 9);
        if (distance != 1)
            throw new AssertionError("The distance between 10 and 9 should be 1, but it is: " + distance);
        distance = sap.length(12, 13);
        if (distance != 4)
            throw new AssertionError("The distance between 12 and 13 should be 4, but it is: " + distance);
        ancestor = sap.ancestor(10, 11);
        if (ancestor != 11)
            throw new AssertionError("The value of ancestor between 10 and 11 should be 11, but it is: " + ancestor);
        distance = sap.length(10, 11);
        if (distance != 1)
            throw new AssertionError("The distance between 10 and 11 should be 1, but it is: " + distance);
        ancestor = sap.ancestor(11, 10);
        if (ancestor != 11)
            throw new AssertionError("The value of ancestor between 11 and 10 should be 11, but it is: " + ancestor);
        distance = sap.length(11, 10);
        if (distance != 1)
            throw new AssertionError("The distance between 11 and 10 should be 1, but it is: " + distance);
        distance = sap.length(8, 13);
        if (distance != 5)
            throw new AssertionError("The distance between 8 and 13 should be 5, but it is: " + distance);
        distance = sap.length(9, 2);
        if (distance != -1) throw new AssertionError("The distance between 9 and 2 should be -1 since they are " +
                "not connected, but it actually is: " + distance);
        ancestor = sap.ancestor(9, 2);
        if (ancestor != -1) throw new AssertionError("The ancestor between the nodes 9 and 2 should be -1" +
                "since they are not connected, but it actually comes up as: " + ancestor);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 4 ----------------------------------");
        digraph = new Digraph(new In("digraph4.txt"));
        sap = new SAP(digraph);
        distance = sap.length(1, 4);
        if (distance != 3) throw new AssertionError("The distance between 1 and 4 should be 3, but it is: " + distance);
        distance = sap.length(4, 1);
        if (distance != 3) throw new AssertionError("The distance between 4 and 1 should be 3, but it is: " + distance);
        distance = sap.length(9, 3);
        if (distance != 6) throw new AssertionError("The distance between 9 and 3 should be 6, but it is: " + distance);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 5 ----------------------------------");
        digraph = new Digraph(new In("digraph5.txt"));
        sap = new SAP(digraph);
        distance = sap.length(17, 21);
        if (distance != 5)
            throw new AssertionError("The distance between 17 and 21 should be 5, but it is: " + distance);
        distance = sap.length(14, 21);
        if (distance != 8)
            throw new AssertionError("The distance between 14 and 21 should be 8, but it is: " + distance);
        distance = sap.length(9, 20);
        if (distance != 3)
            throw new AssertionError("The distance between 9 and 20 should be 3, but it is: " + distance);
        ancestor = sap.ancestor(10, 12);
        if (ancestor != 10) throw new AssertionError("The value of ancestor should be 10, and it is: " + ancestor);
        distance = sap.length(10, 12);
        if (distance != 2)
            throw new AssertionError("The distance between 10 and 12 should be 2, but it is: " + distance);
        distance = sap.length(21, 0);
        if (distance != 1)
            throw new AssertionError("The distance between 21, and 0 should be 1, and it is: " + distance);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 6 ----------------------------------");
        digraph = new Digraph(new In("digraph6.txt"));
        sap = new SAP(digraph);
        distance = sap.length(0, 5);
        if (distance != 5) throw new AssertionError("The distance between 0 and 5 should be 5, but it is: " + distance);
        distance = sap.length(7, 3);
        if (distance != 1) throw new AssertionError("The distance between 7 and 3 should be 1, but it is: " + distance);
        ancestor = sap.ancestor(7, 3);
        if (ancestor != 3)
            throw new AssertionError("The ancestor  between 7 and 3 should be 3, but it is: " + ancestor);
        distance = sap.length(7, 7);
        if (distance != 0)
            throw new AssertionError("The distance between 7, and 7 should be 0, but it is: " + distance);
        distance = sap.length(1, 7);
        if (distance != 3) throw new AssertionError("The distance between 1 and 7 should be 3, but it is: " + distance);
        distance = sap.length(4, 4);
        if (distance != 0)
            throw new AssertionError("The distance between the same node is should be 0, but it is: " + distance);
        StdOut.println("----------------------------------Running AutoGrader Tests for Digraph 9 ----------------------------------");
        digraph = new Digraph(new In("digraph9.txt"));
        sap = new SAP(digraph);
        distance = sap.length(7, 8);
        if (distance != -1)
            throw new AssertionError("The distance between 7 and 8 should be -1, but it is: " + distance);
        distance = sap.length(7, 4);
        if (distance != 3)
            throw new AssertionError("The distance between 7, and 4 should be 3, but it is: " + distance);
        distance = sap.length(4, 0);
        if (distance != 3)
            throw new AssertionError("The distance between 4, and 0 should be 3, but it is: " + distance);
        distance = sap.length(7, 3);
        if (distance != 2)
            throw new AssertionError("The distance between 7, and 3 should be 2, but it is: " + distance);
        distance = sap.length(4, 0);
        if (distance != 3) throw new AssertionError("The distance between 4 and 0 should be 3, but it is: " + distance);
        distance = sap.length(4, 3);
        if (distance != 1)
            throw new AssertionError("The distance between 4, and 3 should be 1, but it is: " + distance);
        distance = sap.length(0, 3);
        if (distance != 1)
            throw new AssertionError("The distance between 0, and 3 should be 1, but it is: " + distance);
        ancestor = sap.ancestor(0, 3);
        if (ancestor != 0)
            throw new AssertionError("The ancestor between 0, and 3 should be 0, but it is: " + ancestor);
        distance = sap.length(0, 5);
        if (distance != 4) throw new AssertionError("The distance between 0 and 5 should be 4, but it is: " + distance);
    }
}
