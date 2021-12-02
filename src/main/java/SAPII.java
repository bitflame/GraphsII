import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SAPII {
    private Digraph digraph;
    boolean[] onStack;
    boolean hasCycle = false;

    public SAPII(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        onStack = new boolean[digraph.V()];
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
        this.digraph = digraph;
    }

    private class Node {
        private final int id;
        private final SAPII.Node prevNode;
        private final int movesTaken;
        private final int movesRemaining;

        public Node(int id, SAPII.Node prevNode, int taken, int remaining) {
            this.id = id;
            this.prevNode = prevNode;
            this.movesTaken = taken;
            this.movesRemaining = remaining;
        }
    }

    public Iterable<Integer> getPath(int from, int to) {
        List<Integer> path = new ArrayList<Integer>();
        DeluxBFS fDBS = new DeluxBFS(digraph, from);
        DeluxBFS fRevDBS = new DeluxBFS(digraph.reverse(), from);
        DeluxBFS tDBS = new DeluxBFS(digraph, to);
        DeluxBFS tRevDBS = new DeluxBFS(digraph, to);
        MinPQ<Node> fromQueue = new MinPQ<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                // number of moves the parent has made plus 1 plus the number moves I have to take from where I am
                if (o1.prevNode.movesTaken + 1 + o1.movesRemaining > o2.prevNode.movesTaken + 1 + o2.movesRemaining)
                    return 1;
                else if (o2.prevNode.movesTaken + 1 + o2.movesRemaining > o1.prevNode.movesTaken + 1 + o1.movesRemaining)
                    return -1;
                return 0;
            }
        });
        Node fromNode = new Node(from, null, 0, tDBS.distTo(to));
        for (int i = 0; i < digraph.V(); i++) {
            if (fDBS.hasPathTo(i)) {
                Node newNode = new Node(i, fromNode, fDBS.distTo(i), fDBS.distTo(to));
                fromQueue.insert(newNode);
            }
        }
        Node minNode = fromQueue.delMin();

        for (int i : fDBS.pathTo(from)) {
            Node newNode = new Node(i, null, 0, tDBS.distTo(i));
            fromQueue.insert(newNode);
        }
        for (int i : fRevDBS.pathTo(from)) {
            Node newNode = new Node(i, null, 0, tDBS.distTo(i));
            fromQueue.insert(newNode);
        }
        return path;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAPII sapii = new SAPII(digraph);
        sapii.getPath(9, 5);
    }
}
