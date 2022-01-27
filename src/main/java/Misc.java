import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

public class Misc {
    private static final int INFINITY = Integer.MAX_VALUE;

    public boolean testEdgeTo(int[] edgeTo, int ancestor, int destination) {
        if (ancestor == destination) return true;
        int i = ancestor;
        for (; i != destination; i = edgeTo[i]) {
            System.out.print(" " + i);
            if (i == INFINITY) return false;
        }
        System.out.println();
        return (i == destination);
    }

    public static void main(String[] args) {
        // (8, 13) with ancestor: 8 - should pass
        int[] edgeTo_8_13_pass = {14, -1, -1, -1, -1, -1, -1, -1, 12, 8, 9, 0, 11, -1, 13};
        edgeTo_8_13_pass[1] = INFINITY;
        edgeTo_8_13_pass[2] = INFINITY;
        edgeTo_8_13_pass[3] = INFINITY;
        edgeTo_8_13_pass[4] = INFINITY;
        edgeTo_8_13_pass[5] = INFINITY;
        edgeTo_8_13_pass[6] = INFINITY;
        edgeTo_8_13_pass[7] = INFINITY;
        edgeTo_8_13_pass[13] = INFINITY;
        // (0, 5) with ancestor being 4 - should pass
        int[] edgeTo_0_5_pass = {-1, -1, -1, 6, 3, -1, 0, -1, 5};
        edgeTo_0_5_pass[0] = INFINITY;
        edgeTo_0_5_pass[1] = INFINITY;
        edgeTo_0_5_pass[2] = INFINITY;
        edgeTo_0_5_pass[5] = INFINITY;
        edgeTo_0_5_pass[7] = INFINITY;
        //  (0, 5) with ancestor being 0 - should fail
        int[] edgeTo_0_5_fail = {-1, -1, 3, 6, 3, -1, 0, -1, 5};
        Misc misc = new Misc();

        System.out.printf("eight has path to thirteen: %b ", misc.testEdgeTo(edgeTo_8_13_pass, 8, 13));
        System.out.printf(" four's other pointer, five, has a path to itself : %b ", misc.testEdgeTo(edgeTo_0_5_pass, 5, 5));
        System.out.printf("four has path to 0: %b ", misc.testEdgeTo(edgeTo_0_5_pass, 4, 0));
        //todo: the rule should be: ancestor should be pointed to, i.e. have a path to, each end, or each end is the ancestor
    }
}
