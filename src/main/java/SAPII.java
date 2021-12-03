import edu.princeton.cs.algs4.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SAPII {
    private boolean[] marked;
    private int count;
    Digraph digraph;
    private boolean hasCycle;

    public SAPII(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        marked = new boolean[digraph.V()];
        this.digraph = digraph;
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
    }

    private class Node {
        private final int id;
        private final Node prevNode;
        private final int movesTaken;
        private final int movesRemaining;

        public Node(int id, Node prevNode, int taken, int remaining) {
            this.id = id;
            this.prevNode = prevNode;
            this.movesTaken = taken;
            this.movesRemaining = remaining;
        }
    }

    public Iterable<Integer> getPath(int from, int to) {

        List<Integer> path = new ArrayList<Integer>();
        List<Integer> nodes = new ArrayList<>(Arrays.asList(from, to));
        /*check the two below for the shortest path*/
        DeluxBFS fromDBFS = new DeluxBFS(digraph, from);
        DeluxBFS toDBFS = new DeluxBFS(digraph, to);
        marked[from] = true;
        for (int i = 0; i < digraph.V(); i++) {
            System.out.println("5 has path to " + i + toDBFS.hasPathTo(i));
        }

        return path;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAPII sapii = new SAPII(digraph);
        sapii.getPath(9, 5);
    }
}
