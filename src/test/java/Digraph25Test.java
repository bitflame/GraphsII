import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph25Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // System.out.println("length between 1 and 3, should be 2: " + sap.length(1, 3));
        // System.out.println("length between 1 and 2, should be 1: " + sap.length(1, 2));
        if (sap.length(17, 24) != 5) {
            System.out.printf(" the value of length() between 17 and 24 is : %d\n", sap.length(17, 24));
            throw new AssertionError("but it should be 5");
        }
    }
}