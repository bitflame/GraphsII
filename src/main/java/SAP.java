import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.io.File;
import java.util.*;

public class SAP {
    boolean hasCycle = false;
    private Digraph digraph;
    int ancestor = -1;
    int from;
    int to;
    List<Integer> shortPath;
    boolean[] onStack;
    //boolean[] marked;
    //int[] edgeTo;
    //int[] distTo;
    boolean stop = false;


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
        onStack = new boolean[digraph.V()];
        //marked = new boolean[digraph.V()];
        //edgeTo = new int[digraph.V()];
        //distTo = new int[digraph.V()];
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
        this.digraph = digraph;
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (getPath(v, w) != null) return getPath(v, w).size();
        else return -1;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.");
        List<List<Integer>> paths = new ArrayList<>();
        List<Integer> singlePath = new ArrayList<>();
        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException("None of the values in subsets to length() can be null.");
            for (Integer j : w) {
                if (j == null)
                    throw new IllegalArgumentException("None of the values in subsets to length() can be null.");
                if (getPath(i, j) != null) return getPath(i, j).size();
            }
        }
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if ((v == from || v == to) && (w == from || w == to)) return ancestor;
        if (getPath(v, w) != null) getPath(v, w);
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.");
        int currentAncestor = ancestor, previousAncestor = ancestor;
        List<Integer> prevShortPath = getPath(v.iterator().next(), w.iterator().next());
        previousAncestor = ancestor;
        List<Integer> currShortPath = null;
        for (int i : v) {
            for (int j : w) {
                SAP s = new SAP(digraph);
                currShortPath = s.getPath(i, j);
                currentAncestor = s.ancestor;
                if (prevShortPath.size() > currShortPath.size()) {
                    previousAncestor = currentAncestor;
                    prevShortPath = currShortPath;
                }
            }
        }
        return previousAncestor;
    }

    private List<Integer> getPath(int from, int to) {
        if (to < 0 || to >= digraph.V())
            throw new IllegalArgumentException("vertex " + to + " is not between 0 and " + (digraph.V() - 1));
        if (from < 0 || from >= digraph.V())
            throw new IllegalArgumentException("vertex " + from + " is not between 0 and " + (digraph.V() - 1));
        if ((from == this.from || from == this.to) && (to == this.to || to == this.from)) return shortPath;
        if (from == to) {
            shortPath = new ArrayList<>();
            shortPath.add(from);
            ancestor = from;
            return shortPath;
        }
        if ((digraph.outdegree(to) == 0 && digraph.indegree(to) == 0) || (digraph.outdegree(from) == 0 &&
                digraph.indegree(from) == 0)) return null;
        shortPath = new ArrayList<>();
        this.from = from;
        this.to = to;
        DeluxBFS fDBS = new DeluxBFS(digraph, from);
        DeluxBFS tDBS = new DeluxBFS(digraph, to);
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
        MinPQ<Node> toQueue = new MinPQ<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.prevNode.movesTaken + 1 + o1.movesRemaining > o2.prevNode.movesTaken + 1 + o2.movesRemaining)
                    return 1;
                else if (o2.prevNode.movesTaken + 1 + o2.movesRemaining > o1.prevNode.movesTaken + 1 + o1.movesRemaining)
                    return -1;
                return 0;
            }
        });
        Node fromNode = new Node(from, null, 0, tDBS.distTo(from));
        fromQueue.insert(fromNode);
        Node toNode = new Node(to, null, 0, fDBS.distTo(to));
        toQueue.insert(toNode);
        Node minFNode = fromQueue.delMin();
        onStack[minFNode.id] = true;
        Node minTNode = toQueue.delMin();
        onStack[minTNode.id] = true;
        Node newNode;
        /* Need to populate fromQueue and toQueue here once b/c grandparent is null, and throws an exception when I check
         * for A*'s problem below . Populate the queues with forward and reverse nodes each time; you can also do this
         * only when there is a cycle */
        for (int i : digraph.adj(minFNode.id)) {
            newNode = new Node(i, minFNode, minFNode.movesTaken + 1, tDBS.distTo(i));
            fromQueue.insert(newNode);
        }
        if (!fromQueue.isEmpty()) {
            minFNode = fromQueue.delMin();
            if (onStack[minFNode.id]) {
                shortPath = extractPath(minFNode, minTNode, minFNode.id);
                Collections.sort(shortPath);
                return shortPath;
            }
            onStack[minFNode.id] = true;
        }

        /* populate ToQueue */
        for (int i : digraph.adj(minTNode.id)) {
            if (i != to) {
                newNode = new Node(i, minTNode, minTNode.movesTaken + 1, fDBS.distTo(i));
                toQueue.insert(newNode);
            }
        }
        if (!toQueue.isEmpty()) {
            minTNode = toQueue.delMin();
            if (onStack[minTNode.id]) {
                shortPath = extractPath(minFNode, minTNode, minTNode.id);
                Collections.sort(shortPath);
                return shortPath;
            }
            onStack[minTNode.id] = true;
        }
        Node previousFromNode;
        Node previousToNode;
        while (stop == false) {
            previousToNode = minTNode;
            previousFromNode = minFNode;
            for (int i : digraph.adj(minFNode.id)) {
                if (i != minFNode.prevNode.id) { // to address A*'s problem with the node before parent
                    newNode = new Node(i, minFNode, minFNode.movesTaken + 1, tDBS.distTo(i));
                    fromQueue.insert(newNode);
                }
            }
            if (!fromQueue.isEmpty()) {
                minFNode = fromQueue.delMin();
                if (onStack[minFNode.id]) {
                    while (minTNode.id != minFNode.id) minTNode = minTNode.prevNode;
                    stop = true;
                    shortPath = extractPath(minFNode, minTNode, minFNode.id);
                    Collections.sort(shortPath);
                    return shortPath;
                }
                onStack[minFNode.id] = true;
            }

            for (int i : digraph.adj(minTNode.id)) {
                if (i != minTNode.prevNode.id) {
                    newNode = new Node(i, minTNode, minTNode.movesTaken + 1, fDBS.distTo(i));
                    toQueue.insert(newNode);
                }
            }
            if (!toQueue.isEmpty()) {
                minTNode = toQueue.delMin();
                if (onStack[minTNode.id]) {
                    while (minTNode.id != minFNode.id) minFNode = minFNode.prevNode;
                    stop = true;
                    shortPath = extractPath(minFNode, minTNode, minTNode.id);
                    Collections.sort(shortPath);
                    return shortPath;
                }
                onStack[minTNode.id] = true;
            }/* if the nodes do not change don't stay in the loop. Reasons are either the queues are empty or there are
            no more adjacent nodes or both */
            if (minFNode == previousFromNode && minTNode == previousToNode) break;
        }
        return shortPath;
    }



    private List<Integer> extractPath(Node minF, Node minT, int match) {
        List<Integer> path = new ArrayList<>();
        ancestor = match; // ancestor should be the first match
        while (minF != null && minF.prevNode != null) {
            if (!path.contains(minF.id)) {
                path.add(minF.id);
            }
            minF = minF.prevNode;
        }
        while (minT.prevNode != null) {
            if (!path.contains(minT.id)) {
                path.add(minT.id);
            }
            minT = minT.prevNode;
        }
        if (!path.contains(from)) path.add(from);
        if (!path.contains(to)) path.add(to);
        return path;
    }

    public static void main(String[] args) {
        /*
        System.out.println(sap.ancestor(1, 2));
        System.out.println(sap.ancestor(0, 2));
        System.out.println(sap.ancestor(0, 1));
        System.out.println(sap.ancestor(0, 10));
Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAP sap = new SAP(digraph);
        sap.getPathTwo(9, 5);
        sap.ancestor(1, 2);
        sap.ancestor(0, 24);
        [13, 23, 24] | [6, 16, 17] | [3]


        digraph = new Digraph(new In(new File("src/main/resources/digraph1.txt")));
        sap = new SAP(digraph);
        System.out.println("Here is result of 1 and 6: " + sap.ancestor(1, 6));*/
        /* Reading in digraph25.txt here */

        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        System.out.print("The path between 2 and 0 should be: [ 0 2 ] ");
        System.out.print("[");
        for (int i : sap.getPath(2, 0)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(2, 0));
        System.out.println();
        System.out.print("The path between 1 and 2 should be [0 1 2] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(1, 2));
        System.out.println();
        System.out.print("The path between 3 and 4 should be [1 3 4] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(3, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(3, 4));
        System.out.println();
        System.out.print("The path between 4 and 3 should be [1 3 4] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(4, 3)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(4, 3));
        System.out.println();
        System.out.print("The path between 5 and 6 should be [5 2 6] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(5, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(5, 6));
        System.out.println();
        System.out.print("The path between 6 and 5 should be [ 2 5  6] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(6, 5)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(6, 5));
        System.out.println();
        System.out.print("The path between 4 and 6 should be: [  0 1 2 4 6 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(4, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(4, 6));
        System.out.println();
        System.out.print("The path between 1 and 6 should be: [  0 1 2 6 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(1, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(1, 6));
        System.out.println();
        System.out.print("The path between 17 and 24 should be: [  5 10 12 17 20 24 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(17, 24)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(17, 24));
        System.out.println();
        System.out.print("The path between 23 and 24 should be: [ 24 23 20 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(23, 24)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.println("And the ancestor is : " + sap.ancestor(23, 24));
        System.out.println();
        System.out.print("The path between 11 and 4 should be: [  11 5 4 2 1 0 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(11, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(11, 4));
        System.out.println();
        System.out.print("The path between 17 and 19 should be: [ 17 5 10 12 19 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(17, 19)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(17, 19));
        System.out.println();
        System.out.print("The path between 17 and 17 should be: [ 17 ] ");
        System.out.print("[");
        sap = new SAP(digraph);
        for (int i : sap.getPath(17, 17)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        sap = new SAP(digraph);
        System.out.println("And the ancestor is : " + sap.ancestor(17, 17));
        System.out.println();
        sap = new SAP(digraph);
        List<Integer> one = new ArrayList<>(Arrays.asList(13, 23, 24));
        List<Integer> two = new ArrayList<>(Arrays.asList(6, 16, 17));
        System.out.println("==========================================================================================");
        System.out.println("The shortest common ancestor in above sets should be 3, and it is: " + sap.ancestor(one, two));
        System.out.println("==========================================================================================");
        digraph = new Digraph(new In(new File("src/main/resources/digraph1.txt")));
        sap = new SAP(digraph);
        System.out.print("The path between 10 and 4 should be: [ 4 1 5 10 ] ");
        System.out.print("[");
        for (int i : sap.getPath(4, 10)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        sap = new SAP(digraph);
        System.out.print("The path between 7 and 2 should be: [ 0 1 2 3 7 ] ");
        System.out.print("[");
        for (int i : sap.getPath(7, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        sap = new SAP(digraph);
        System.out.println("ancestor should return 1 for values 3 and 11: " + sap.ancestor(3, 11));
        System.out.println("********************************* Ambiguous tests ***************************************");
        digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        sap = new SAP(digraph);
        System.out.print("The shortest path between 1 and 2 - in ambiguous-ancestor is [1 2]");
        System.out.print("[");
        // test 1 and 2 for ambiguous-ancestor
        for (int i : sap.getPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(1, 2));
        System.out.println();
        System.out.print("The shortest path between 0 and 2 - in ambiguous-ancestor is [0 1 2]");
        System.out.print("[");
        // test 0 and 2 for ambiguous-ancestor
        sap = new SAP(digraph);
        for (int i : sap.getPath(0, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println("And the ancestor is : " + sap.ancestor(0, 2));
        System.out.println();
        System.out.print("The shortest path between 9 and 5 - in ambiguous-ancestor is [] - an empty set. There is not path : ");
        System.out.print("[");
        // find the shortest path between 9 and 5 for ambiguous-ancestor
        sap = new SAP(digraph);
        for (int i : sap.getPath(9, 5)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("The path between 27 and 0 should be: Exception ");
        sap = new SAP(digraph);
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
        digraph = new Digraph(new In(new File("src/main/resources/simplecycle.txt")));
        sap = new SAP(digraph);
        System.out.println("Expecting this to be true for simplecycle.txt: " + sap.hasCycle);
    }
}
