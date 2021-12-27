import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph6Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // System.out.println("length between 1 and 3, should be 2: " + sap.length(1, 3));
        // System.out.println("length between 1 and 2, should be 1: " + sap.length(1, 2));
        if (sap.length(1, 5) != 4) {
            System.out.printf(" the value of length() between 1 and 5 is : %d\n", sap.length(1, 5));
            throw new AssertionError("but it should be 4");
        }
    }
}
