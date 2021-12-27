import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph1Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        //System.out.println(" The ancestor between 2 and 6 is: " + sap.ancestorII(2, 6) + " Minimum Distance: " + sap.lengthII(2,6));
        //System.out.println(" The ancestor between 2 and 0 is: " + sap.ancestorII(2, 0) + " Minimum Distance: " + sap.lengthII(2,6));
        if (sap.length(2, 6) != -1)
            throw new AssertionError("the value of length() to nonexistent node should be -1");
        if (sap.length(2, 0) != 1) throw new AssertionError("the value of length() between 2 and 0 should be 1");
        if (sap.length(1, 0) != 1) throw new AssertionError("the value of length() between 1 and 0 should be 1");
        if (sap.length(4, 1) != 1) throw new AssertionError("the value of length() between 4 and 1 should be 1");
        if (sap.length(7, 9) != 4) throw new AssertionError("the value of length() between 7 and 9 should be 4");
        if (sap.length(10, 1) != 2) throw new AssertionError("the value of length() between 10 and 1 should be 2");
        if (sap.length(12, 9) != 3) throw new AssertionError("the value of length() between 12 and 9 should be 3");
        // if (sap.length(2, 0) != 45) throw new AssertionError("why isn't minDistance value = 45? ");
    }
}
