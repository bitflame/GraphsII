import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph2Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // System.out.println("length between 1 and 3, should be 2: " + sap.length(1, 3));
        // System.out.println("length between 1 and 2, should be 1: " + sap.length(1, 2));
        if (sap.length(1, 3) != 2) throw new AssertionError("the value of length() between 1 and 3 should be 2");
        if (sap.length(1, 2) != 1) throw new AssertionError("the value of length() between 1 and 2 should be 1");
        if (sap.length(1, 4) != 3) throw new AssertionError("the value of length() between 1 and 4 should be 3");
        if (sap.length(1, 5) != 4) throw new AssertionError("the value of length() between 1 and 5 should be 4");
        if (sap.length(1, 3) != 2) throw new AssertionError("the value of length() between 1 and 3 should be 2");
        if (sap.length(1, 0) != 1) throw new AssertionError("the value of length() between 1 and 0 should be 1");
    }
}
