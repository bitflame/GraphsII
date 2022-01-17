import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph3Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // System.out.println("length between 1 and 3, should be 2: " + sap.length(1, 3));
        // System.out.println("length between 1 and 2, should be 1: " + sap.length(1, 2));
        System.out.println("--------------------------Testing digraph3----------------");

        int distance = sap.length(10, 9);
        if (distance != 1)
            throw new AssertionError("The first time running length() for Digraph3Test. The value of " +
                    "length(10, 9) should be 1 but it is: " + distance);

         distance = sap.length(9, 10);
        if (distance != 1)
            throw new AssertionError("The first time running length() for Digraph3Test. The value of " +
                    "length(10, 9) should be 1 but it is: " + distance);

        distance = sap.length(8, 14);
        if (distance != 4) throw new AssertionError("The length between 8 amd 14 should be 4, but it is: " + distance);

        distance = sap.length( 14,8);
        if (distance != 4) throw new AssertionError("The length between 14 amd 8 should be 4, but it is: " + distance);

        distance = sap.length(12, 11);
        if (distance != 1) throw new AssertionError("The length between 12 and 11 should be 1, but it is: " + distance);

        distance = sap.length(11, 12);
        if (distance != 1) throw new AssertionError("The length between 11 and 12 should be 1, but it is: " + distance);

        distance = sap.length(12, 13);
        if (distance != 4) throw new AssertionError("The length between 12 and 13 should be 4, but it is: " + distance);
    }
}
