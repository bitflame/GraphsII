import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph1Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        // System.out.println(sap.length(2, 6));
        int result = sap.length(2, 6);
        if (result != -1) throw new AssertionError("the value of length() to nonexistent node should be -1");
        // if (result != 1) throw new AssertionError("the value of length() to nonexistent node should be -1");
    }
}
