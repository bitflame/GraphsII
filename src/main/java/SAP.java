import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SAP {
    boolean hasCycle;
    private Digraph digraph;

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

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
        this.digraph = digraph;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return getPath(v, w).size();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        DeluxBFS pathToFrom = new DeluxBFS(digraph, v);
        DeluxBFS pathToTo = new DeluxBFS(digraph, w);
        for (int i = digraph.V() - 1; i >= 0; i--) {
            if (pathToFrom.hasPathTo(i) && pathToTo.hasPathTo(i)) {
                return i;
            }
        }
        return -1;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.");
        List<List<Integer>> paths = new ArrayList<>();
        List<Integer> singlePath = new ArrayList<>();
        for (int i : v) {
            for (int j : w) {
                if (getPath(i, j) != null) return getPath(i, j).size();
            }
        }
        return -1;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // Note - this won't work if the first match is not the shortest path but not other info is in Iterable
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.");
        for (int i : v) {
            for (int j : w) {
                if (i == j) return j;
            }
        }
        return -1;
    }


    public List<Integer> getPath(int from, int to) {
        List<Integer> shortestPath = new ArrayList<>();
        List<Integer> sources = new ArrayList<Integer>(Arrays.asList(from, to));
        // get the path from each point to other points in the graph starting from zero, and collect only the
        // nodes common in both paths, and the least distance
        // If there are two of them add both, if there is only one add it and return shortest path
        // List of node, and distance
        DeluxBFS pathToFrom = new DeluxBFS(digraph, from);
   /*   print what is in these two; up and down the line and see if you can filter out unwanted nodes from the sources
        path
        System.out.println("\nHere is everything in pathToFrom: ");
        for (int v = 0; v < digraph.V() ; v++) {
            if (pathToFrom.hasPathTo(v)) {
                for (int i:pathToFrom.pathTo(v)) {
                    System.out.print(i);
                }
            }
        }
Go through From and To paths in one loop, if the values are different push to stack, and go on, if they are the same,
 push to stack and return*/
        DeluxBFS pathToTo = new DeluxBFS(digraph, to);
        int toDistance = 0, frDistance = 0;
        //System.out.println("Here is everything in pathToTo");
        for (int v = digraph.V() - 1; v >= 0; v--) {
            /*if (pathToFrom.hasPathTo(to) && pathToFrom.distTo(to) == 1) {
                shortestPath.add(from);
                shortestPath.add(to);
                return shortestPath;
            }*/
            if (pathToTo.hasPathTo(v) && !pathToFrom.hasPathTo(v)) {
                // todo - Somehow check to see if v gets me closer to from or the common ancestor
                shortestPath.add(v);
                frDistance = pathToTo.distTo(v);
            }
            if (!pathToTo.hasPathTo(v) && pathToFrom.hasPathTo(v)) {
                /* this rule only works if path to From is going to end up on a common node; one that is connected to
                 * path of to*/
                // todo - Somehow check to see if v gets me closer to to or the common ancestor
                shortestPath.add(v);
                toDistance = pathToFrom.distTo(v);
            }
            // if I save this last match only keep the minimum, it might fix the ambiguity issue
            if (pathToFrom.hasPathTo(v) && pathToTo.hasPathTo(v)) {
                shortestPath.add(v);
                return shortestPath;
            }
        }
        return shortestPath;
    }

    public Iterable<Integer> shortestPath(int from, int to) {
        List<Integer> sPath = new ArrayList<>();
        /* Build a min priority queue for to and from based on the node's distance to each node, then use A*:-) to
         * process nodes */
        MinPQ<Node> fromQueue = new MinPQ<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.prevNode.movesTaken + 1 + o1.movesRemaining > o2.prevNode.movesTaken + 1 + o2.movesRemaining)
                    return 1;
                else if (o2.prevNode.movesTaken + 1 + o2.movesRemaining > o1.prevNode.movesTaken + 1 + o1.movesRemaining)
                    return -1;
                return 0;
            }
        });
        MinPQ<Node> toQueue = new MinPQ<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.prevNode.movesTaken + 1 + o1.movesRemaining > o2.prevNode.movesTaken + 1 + o1.movesRemaining)
                    return 1;
                else if (o2.prevNode.movesTaken + 1 + o1.movesRemaining > o1.prevNode.movesTaken + 1 + o1.movesRemaining)
                    return -1;
                return 0;
            }
        });
        List<Integer> sources = new ArrayList<>();
        sources.add(from);
        sources.add(to);
        DeluxBFS dBFS = new DeluxBFS(digraph, sources);
        DeluxBFS fDBS = new DeluxBFS(digraph, from);
        DeluxBFS tDBS = new DeluxBFS(digraph, to);
        /* The last value might also be tDBS.distTo(from) todo: check the numbers tomorrow */
        Node toNode = new Node(to, null, 0, tDBS.distTo(from));
        fromQueue.insert(toNode);
        Node minNode = fromQueue.delMin();
        while (minNode.id != from) {
            // I do not know if I should look at adjacency's of each node or tDBS pathTo. I'll go with pathTo() for now
            // for (int i : digraph.adj(minNode.id)) {
            for (int i : tDBS.pathTo(minNode.id)) {
                if (i != to) { // to address A*'s problem with the node before parent
                    Node newNode = new Node(i, minNode, minNode.movesTaken + 1, tDBS.distTo(from));
                    fromQueue.insert(newNode);
                }
            }
            minNode=fromQueue.delMin();
        }
        if (minNode.id == from) {
            while (minNode.id != toNode.id) {
                sPath.add(minNode.id);
                minNode = minNode.prevNode;
            }
        }

        /*It seems like you have to save the path you got above, and do another loop from 'from' node to 'to' node and
         * and take the shorter path */

        return sPath;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        System.out.print("The path between 1 and 2 should be [0 1 2] ");
        System.out.print("[");
        for (int i : sap.getPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 3 and 4 should be [1 3 4] ");
        System.out.print("[");
        for (int i : sap.getPath(3, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 4 and 3 should be [1 3 4] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 3)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 5 and 6 should be [5 2 6] ");
        System.out.print("[");
        for (int i : sap.getPath(5, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 6 and 5 should be [ 5 2 6] ");
        System.out.print("[");
        for (int i : sap.getPath(6, 5)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 4 and 6 should be: [  0 1 2 4 6 ] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 1 and 6 should be: [  0 1 2 6 ] ");
        System.out.print("[");
        for (int i : sap.getPath(1, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 17 and 24 should be: [  5 10 12 17 20 24 ] ");
        System.out.print("[");
        for (int i : sap.getPath(17, 24)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        System.out.println();
        System.out.print("The path between 23 and 24 should be: [ 24 23 20 ] ");
        System.out.print("[");
        for (int i : sap.getPath(23, 24)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();


        System.out.print("The path between 11 and 4 should be: [  11 5 4 2 1 0 ] ");
        System.out.print("[");
        for (int i : sap.getPath(11, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        System.out.print("The path between 17 and 19 should be: [ 17 5 10 12 19 ] ");
        System.out.print("[");
        for (int i : sap.getPath(17, 19)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        System.out.print("The path between 17 and 17 should be: [ 17 ] ");
        System.out.print("[");
        for (int i : sap.getPath(17, 17)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        digraph = new Digraph(new In(new File("src/main/resources/digraph1.txt")));
        sap = new SAP(digraph);

        System.out.print("The path between 10 and 4 should be: [ 4 1 5 10 ] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 10)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();


        System.out.println("ancestor should return 1 for values 3 and 11: " + sap.ancestor(3, 11));

        digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        sap = new SAP(digraph);
        System.out.print("The shortest path between 1 and 2 - in ambiguous-ancestor is [1 2]");
        System.out.print("[");
        // test 1 and 2 for ambiguous-ancestor
        for (int i : sap.getPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        System.out.print("The shortest path between 0 and 2 - in ambiguous-ancestor is [0 1 2]");
        System.out.print("[");
        // test 1 and 2 for ambiguous-ancestor
        for (int i : sap.getPath(0, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        digraph = new Digraph(new In(new File("src/main/resources/simplecycle.txt")));
        sap = new SAP(digraph);
        System.out.println("Expecting this to be true for simplecycle.txt: " + sap.hasCycle);

        System.out.print("The path between 27 and 0 should be: Exception ");
        System.out.print("[");
        try {
            for (int i : sap.getPath(27, 0)) {
                System.out.print(" " + i + " ");
            }
            System.out.println("]");
            System.out.println();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }
}
