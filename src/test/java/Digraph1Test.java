import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Digraph1Test {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        //System.out.println(" The ancestor between 2 and 6 is: " + sap.ancestorII(2, 6) + " Minimum Distance: " + sap.lengthII(2,6));
        //System.out.println(" The ancestor between 2 and 0 is: " + sap.ancestorII(2, 0) + " Minimum Distance: " + sap.lengthII(2,6));
        if (sap.lengthII(2, 6) != -1)
            throw new AssertionError("the value of length() to nonexistent node should be -1");
        if (sap.lengthII(2, 0) != 1) throw new AssertionError("the value of length() to nonexistent node should be -1");
        if (sap.lengthII(2, 0) != 45) throw new AssertionError("why isn't minDistance value = 45? ");
    }
}
