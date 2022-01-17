import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.AssumptionViolatedException;
import org.testng.asserts.Assertion;

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
        int ancestor = sap.ancestor(1, 2);
        if (ancestor != 0)
            throw new AssertionError("the value of ancestor(1,2) should be 2 but it is: " + ancestor);
        int dist = sap.length(1, 2);
        if (dist != 2) throw new AssertionError("the value of length(1,2) should be 1 but it is: " +
                dist);
        dist = sap.length(4, 6);
        if (dist != 4) throw new AssertionError("the value of length(4,6) should be 4 but it is: " +
                dist);
        dist = sap.length(22, 0);
        if (dist != 5) throw new AssertionError("the value of length(22,0) should be 5 but it is: " + dist);
        dist = sap.length(0, 22);
        if (dist != 5) throw new AssertionError("the value of length(0,22) should be 5 but it is: " + dist);
    }
}
