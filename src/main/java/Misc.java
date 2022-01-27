import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

public class Misc {

    public boolean testEdgeTo(int[] edgeTo, int ancestor, int destination) {
        if (ancestor == destination) return true;
        int i = ancestor;
        for (; i != destination; i = edgeTo[i]) {
            if (i == -1) break;
            //System.out.print(" " + i);
        }
        if (i != -1) System.out.print(" " + i);
        return (i == destination);
    }

    public static void main(String[] args) {
        // (8, 13) with ancestor: 8 - should pass
        // [14, -1, -1, -1, -1, -1, -1, -1, -1, 8, 9, 0, 11, -1, 13]
        int[] edgeTo_8_13_pass = {14, -1, -1, -1, -1, -1, -1, -1, 12, 8, 9, 0, 11, -1, 13};
        // (0, 5) with ancestor being 4 - should pass
        int[] edgeTo_0_5_pass = {-1, -1, -1, 6, 3, -1, 0, -1, 5};
        //  (0, 5) with ancestor being 0 - should fail
        int[] edgeTo_0_5_fail = {-1, -1, 3, 6, 3, -1, 0, -1, 5};
        Misc misc = new Misc();
        System.out.printf(" eight has path to thirteen: %b \n", misc.testEdgeTo(edgeTo_8_13_pass, 8, 13));
        System.out.printf(" eight has path to eight: %b \n", misc.testEdgeTo(edgeTo_8_13_pass, 8, 8));
        System.out.printf(" four's other pointer, five, has a path to itself : %b \n", misc.testEdgeTo(edgeTo_0_5_pass, 5, 5));
        System.out.printf(" four has path to 0: %b \n", misc.testEdgeTo(edgeTo_0_5_pass, 4, 0));
        System.out.printf(" 4 should have a path to 5 in digraph9: %b\n", misc.testEdgeTo(edgeTo_0_5_fail, 4, 5));
        /* todo: the rule should be: ancestor should be pointed to, i.e. have a path to, each end, or each end is the
            ancestor. either ancestor or its edgeTo should have a  path or be pointed to by each end or their children */
    }
}
