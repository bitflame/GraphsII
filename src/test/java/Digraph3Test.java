import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph3Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // System.out.println("length between 1 and 3, should be 2: " + sap.length(1, 3));
        // System.out.println("length between 1 and 2, should be 1: " + sap.length(1, 2));
        if (sap.length(14, 8) != 4) {
            System.out.printf(" the value of length() between 14 and 8 is : %d\n", sap.length(14, 8));
            throw new AssertionError("but it should be 4");
        }
        int distance = sap.length(10, 9);
        if (distance != 1)
            throw new AssertionError("The first time running length() for Digraph3Test. The value of " +
                    "length(10, 9) should be 1 but it is: " + distance);
        distance = sap.length(12, 13);
        if (distance != 4) throw new AssertionError("The length between 12 and 13 should be 4, but it is: " + distance);
    }
}
