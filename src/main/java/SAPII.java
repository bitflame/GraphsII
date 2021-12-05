import edu.princeton.cs.algs4.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SAPII {
    private boolean[] marked;
    private boolean[] revMarked;
    private int[] edgeTo;
    private int[] revEdgeTo;
    private int[] distTo;
    private int[] revDistTo;
    List<Integer> path = new ArrayList<>();
    Digraph digraph;
    int to;
    int from;
    boolean hasCycle = false;

    public SAPII(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        distTo = new int[digraph.V()];
        revDistTo = new int[digraph.V()];
        for (int i = 0; i < digraph.V(); i++) {
            distTo[i] = digraph.V();
        }
        marked = new boolean[digraph.V()];
        revMarked = new boolean[digraph.V()];
        edgeTo = new int[digraph.V()];
        revEdgeTo = new int[digraph.V()];
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
        this.from = from;
        this.to = to;
        DeluxBFS deluxBFS = new DeluxBFS(digraph, nodes);
        DeluxBFS fromBFS = new DeluxBFS(digraph, from);
        for (int i = 0; i < digraph.V(); i++) {
            if (fromBFS.pathTo(i) != null) for (int j : fromBFS.pathTo(i)) {
                path.add(j);
            }
            if (path.contains(to)) break;
            else path = new ArrayList<>();
        }

        for (int i = 0; i < digraph.V(); i++) {
            if (deluxBFS.pathTo(i)!=null) for (int j: deluxBFS.pathTo(i)){
                path.add(j);
            }
            if (path.contains(to) && path.contains(from)) break;
            else path = new ArrayList<>();
        }
        DeluxBFS sixBFS = new DeluxBFS(digraph,6);
        for (int i = 0; i < digraph.V(); i++) {
            if (sixBFS.pathTo(i)!=null) for (int j: sixBFS.pathTo(i)){
                path.add(j);
            }
            if (path.contains(to)&& path.contains(from)) break;
            else path = new ArrayList<>();

        }
        /*check the two below for the shortest path*/
        marked[from] = true;
        for (int i = 0; i < digraph.V(); i++) {
            if (!marked[i]) dfs(i);
        }
        for (int i = 0; i < digraph.V(); i++) {
            if (!revMarked[i]) dfsRev(i);
        }
        for (int i = to; i <= from; i = edgeTo[i]) {
            path.add(i);
        }
        MinPQ<Node> minPQ = new MinPQ<Node>(new Comparator<Node>() {
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
        Node n = new Node(from, null, 0, distTo[to]);
        for (int i = 0; i < digraph.V(); i++) {
            Node node = new Node(i, n, n.movesTaken + 1, distTo[to]);
            minPQ.insert(node);
            n = node;
        }
        n = new Node(to, null, 0, 0);
        for (int i = 0; i < digraph.V(); i++) {
            Node node = new Node(i, n, n.movesTaken + 1, distTo[to]);
            minPQ.insert(node);
            n = node;
        }
        /*You may have to go through all the vertices and add an edge in the reverse direction, and then calculate the
        shortest path.I also need to review how Directed BFS works and look at the example one more time.
        System.out.println("Priority Queue Content: ");
        while (!minPQ.isEmpty()) {
            System.out.print(minPQ.delMin() + " ");
        }
        System.out.println();*/
        return path;
    }

    public void dfs(int v) {
        distTo[v] = 0;
        marked[v] = true;
        Queue<Integer> q = new Queue<>();
        q.enqueue(v);
        while (!q.isEmpty()) {
            int temp = q.dequeue();
            for (int w : digraph.adj(temp)) {
                q.enqueue(w);
                edgeTo[w] = temp;
                marked[w] = true;
                distTo[w] = distTo[temp] + 1;
            }
        }
    }

    public void dfsRev(int v) {
        revDistTo[v] = 0;
        revMarked[v] = true;
        Queue<Integer> q = new Queue<>();
        q.enqueue(v);
        while (!q.isEmpty()) {
            int temp = q.dequeue();
            for (int w : digraph.reverse().adj(v)) {
                if (!revMarked[w]) {
                    q.enqueue(w);
                    revEdgeTo[w] = temp;
                    revMarked[w] = true;
                    revDistTo[w] = revDistTo[temp] + 1;
                }
            }
        }
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAPII sapii = new SAPII(digraph);
        for (int i : sapii.getPath(9, 5)) {
            System.out.println(" " + i);
        }
    }
}
