import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

public class Misc {

    public boolean testEdgeTo(int[] edgeTo, int ancestor, int destination) {
        if (ancestor == destination) return true;
        int i = ancestor;
        for (; i != destination; i = edgeTo[i]) {
            System.out.print(" " + i);
        }
        return (i == destination);
    }

    public static void main(String[] args) {
        // (8, 13) with ancestor: 8 - should pass
        int[] edgeTo_8_13_pass = {14, -1, -1, -1, -1, -1, -1, -1, 12, 8, 9, 0, 11, -1, 13};
        // (0, 5) with ancestor being 4 - should pass
        int[] edgeTo_0_5_pass = {-1, -1, -1, 6, 3, -1, 0, -1, 5};
        //  (0, 5) with ancestor being 0 - should fail
        int[] edgeTo_0_5_fail = {-1, -1, 3, 6, 3, -1, 0, -1, 5};
        Misc misc = new Misc();

        System.out.printf("\n" + misc.testEdgeTo(edgeTo_8_13_pass, 8, 13));
        System.out.printf("\n" + misc.testEdgeTo(edgeTo_0_5_pass, 4, 5));
        // testEdgeTo(ancestor, x) and preAncestor'sNode like v, and w to the other end
    }
}
