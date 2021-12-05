import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SapIII {
    Digraph digraph;
    public Iterable<Integer> getPath(int from, int to){
        List<Integer> path = new ArrayList<Integer>();
        for (int i = 0; i < digraph.V(); i++) {

        }
        return path;
    }
    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAPII sapii = new SAPII(digraph);
        sapii.getPath(9, 5);
    }
}
