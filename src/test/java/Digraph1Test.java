import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph1Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        System.out.println(sap.length(2, 6));
        int result = sap.length(2, 6);
        assert (result == -1);
        assert (result == 1);
    }
}
