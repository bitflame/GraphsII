import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

public class Misc {
    
    public boolean testEdgeTo(int[] edgeTo, int ancestor, int destination) {
        if (ancestor == destination) return true;
        int i = ancestor;
        for (; i != destination; i = edgeTo[i]) {
            System.out.print(" " + i);
        }
        System.out.print(" "+ i);
        System.out.println();
        return (i == destination);
    }

    public static void main(String[] args) {
        // (8, 13) with ancestor: 8 - should pass
        int[] edgeTo_8_13_pass = {14, 0, 0, 0, 0, 0, 0, 0, 12, 8, 9, 0, 11, 0, 13};
        // (0, 5) with ancestor being 4 - should pass
        int[] edgeTo_0_5_pass = {0, 0, 0, 6, 3, 0, 0, 0, 5};
        //  (0, 5) with ancestor being 0 - should fail
        int[] edgeTo_0_5_fail = {0, 0, 3, 6, 3, 0, 0, 0, 5};
        Misc misc = new Misc();
        System.out.printf("eight has path to thirteen: %b \n", misc.testEdgeTo(edgeTo_8_13_pass, 8, 13));
        System.out.printf(" four's other pointer, five, has a path to itself : %b \n", misc.testEdgeTo(edgeTo_0_5_pass, 5, 5));
        System.out.printf("four has path to 0: %b \n", misc.testEdgeTo(edgeTo_0_5_pass, 4, 0));
        System.out.printf("0 has path to 4 in digraph9: %b\n",misc.testEdgeTo(edgeTo_0_5_fail,0,5));
        /* todo: the rule should be: ancestor should be pointed to, i.e. have a path to, each end, or each end is the
            ancestor. Also need to find a way to deal with nodes with no incoming edges vs. nodes that are actually
             pointed to by node id zero */
    }
}
