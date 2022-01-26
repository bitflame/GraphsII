import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph3Test {
    public static void main(String[] args) {
        System.out.println("----------------------Running Digraph3Test---------------------");
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        int distance = sap.length(10, 9);
        if (distance != 1) throw new AssertionError("The length between 10 and 9 should be 1, but it is: " + distance);
        distance = sap.length(9, 10);
        if (distance != 1) throw new AssertionError("The length between 9 and 10 should be 1, but it is: " + distance);
        distance = sap.length(13, 14);
        if (distance != 1) throw new AssertionError("The length between 13 and 14 should be 1, but it is: " + distance);
        distance = sap.length(14, 13);
        if (distance != 1) throw new AssertionError("The length between 14 and 13 should be 1, but it is: " + distance);
        distance = sap.length(13, 0);
        if (distance != 2) throw new AssertionError("The length between 13 and 0 should be 2, but it is: " + distance);
        distance = sap.length(0, 13);
        if (distance != 2) throw new AssertionError("The length between 0 and 13 should 2, but it is: " + distance);
        distance = sap.length(13, 12);
        if (distance != 4) throw new AssertionError("The length between 13 and 13 should be 4, but it is: " + distance);
        distance = sap.length(12, 13);
        if (distance != 4) throw new AssertionError("The length between 12 and 13 should be 4, but it is: " + distance);
        distance = sap.length(7, 10);
        if (distance != 3) throw new AssertionError("The length between 10, and 7 shuld be 3, but it is: " + distance);
        distance = sap.length(10, 7);
        if (distance != 3) throw new AssertionError("The length between 10, and 7 shuld be 3, but it is: " + distance);
        distance = sap.length(5, 12);
        if (distance != -1)
            throw new AssertionError("5 and 12 are not connected, as such the length between them should be -1, but it is: " + distance);
    }
}
