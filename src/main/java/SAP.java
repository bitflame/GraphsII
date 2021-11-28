import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.io.File;
import java.util.*;

public class SAP {
    boolean hasCycle;
    private Digraph digraph;
    int ancestor = -1;

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
                currShortPath = getPath(i, j);
                currentAncestor = ancestor;
                if (prevShortPath.size() > currShortPath.size()) {
                    previousAncestor = currentAncestor;
                    prevShortPath = currShortPath;
                }
            }
        }
        return previousAncestor;
    }


    public List<Integer> getPath(int from, int to) {
        if (to < 0 || to >= digraph.V())
            throw new IllegalArgumentException("vertex " + to + " is not between 0 and " + (digraph.V() - 1));
        if (from < 0 || from >= digraph.V())
            throw new IllegalArgumentException("vertex " + from + " is not between 0 and " + (digraph.V() - 1));
        if ((digraph.outdegree(to) == 0 && digraph.indegree(to) == 0) || (digraph.outdegree(from) == 0 &&
                digraph.indegree(from) == 0)) return null;
        List<Integer> sPath = new ArrayList<>();
        if (from == to) {
            sPath.add(from);
            return sPath;
        }
        List<Integer> sources = new ArrayList<>();
        sources.add(from);
        sources.add(to);
        Collections.sort(sources);
        from = sources.get(0);
        to = sources.get(1);

        DeluxBFS dBFS = new DeluxBFS(digraph, sources);
        DeluxBFS fDBS = new DeluxBFS(digraph, from);
        DeluxBFS tDBS = new DeluxBFS(digraph, to);
        MinPQ<Node> fromQueue = new MinPQ<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                // number of moves the parent has made plus 1 plus the number moves I have to take from where I am
                if (o1.prevNode.movesTaken + 1 + tDBS.distTo(o1.id) > o2.prevNode.movesTaken + 1 + tDBS.distTo(o2.id))
                    return 1;
                else if (o2.prevNode.movesTaken + 1 + tDBS.distTo(o2.id) > o1.prevNode.movesTaken + 1 + tDBS.distTo(o1.id))
                    return -1;
                return 0;
            }
        });
        MinPQ<Node> toQueue = new MinPQ<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.prevNode.movesTaken + 1 + fDBS.distTo(o1.id) > o2.prevNode.movesTaken + 1 + fDBS.distTo(o2.id))
                    return 1;
                else if (o2.prevNode.movesTaken + 1 + fDBS.distTo(o2.id) > o1.prevNode.movesTaken + 1 + fDBS.distTo(o1.id))
                    return -1;
                return 0;
            }
        });
        Node fromNode = new Node(from, null, 0, tDBS.distTo(from));
        fromQueue.insert(fromNode);
        Node toNode = new Node(to, null, 0, fDBS.distTo(to));
        toQueue.insert(toNode);
        Node minFNode = fromQueue.delMin();
        Node minTNode = toQueue.delMin();
        if (minFNode.id == minTNode.id) return sources;
        Node newNode;
        /* Need to populate fromQueue and toQueue here once b/c grandparent is null, and throws an exception when I check
         * for A*'s problem below */
        for (int i : digraph.adj(minFNode.id)) {
            newNode = new Node(i, minFNode, minFNode.movesTaken + 1, tDBS.distTo(i));
            fromQueue.insert(newNode);
        }
        if (!fromQueue.isEmpty()) minFNode = fromQueue.delMin();
        for (int i : digraph.adj(minTNode.id)) {
            newNode = new Node(i, minTNode, minTNode.movesTaken + 1, fDBS.distTo(i));
            toQueue.insert(newNode);
        }
        if (!toQueue.isEmpty()) minTNode = toQueue.delMin();
        while (minFNode.id != minTNode.id) {
            if (!fromQueue.isEmpty()) minFNode = fromQueue.delMin();
            for (int i : digraph.adj(minFNode.id)) {
                if (i != minFNode.prevNode.id) { // to address A*'s problem with the node before parent
                    newNode = new Node(i, minFNode, minFNode.movesTaken + 1, tDBS.distTo(i));
                    fromQueue.insert(newNode);
                }
            }
            if (!toQueue.isEmpty()) minTNode = toQueue.delMin();
            if (minFNode.id == minTNode.id) break;
            for (int i : digraph.adj(minTNode.id)) {
                newNode = new Node(i, minTNode, minTNode.movesTaken + 1, fDBS.distTo(i));
                toQueue.insert(newNode);
            }
        }
        while (minFNode.prevNode != null && minTNode.prevNode != null && minFNode.prevNode.id == minTNode.prevNode.id) {
            minFNode = minFNode.prevNode;
            minTNode = minTNode.prevNode;
        }
        ancestor = minFNode.id; // which should be the same as minFNode.id
        while (true) {
            if (!sPath.contains(minFNode.id)) {
                sPath.add(minFNode.id);
            }
            if (minFNode.prevNode == null && minTNode.prevNode == null) break;
            if (minFNode.prevNode != null) minFNode = minFNode.prevNode;
            /* I can double check here also that I have the shortest path by testing minFNode==minTNode */
            if (minTNode.prevNode != null) {
                minTNode = minTNode.prevNode;
            }
            if (!sPath.contains(minTNode.id)) {
                sPath.add(minTNode.id);
            }
        }
        Collections.sort(sPath);
        return sPath;
    }

    public static void main(String[] args) {
        /*Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAP sap = new SAP(digraph);
        System.out.println(sap.ancestor(1, 2));
        System.out.println(sap.ancestor(0, 2));
        System.out.println(sap.ancestor(0, 1));
        System.out.println(sap.ancestor(0, 10));

        sap.ancestor(1, 2);
        sap.ancestor(0, 24);
        [13, 23, 24] | [6, 16, 17] | [3]
        */

        Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph1.txt")));
        SAP sap = new SAP(digraph);
        System.out.println("Here is result of 1 and 6: " + sap.ancestor(1, 6));
        /* Reading in digraph25.txt here */
        digraph = new Digraph(new In(args[0]));
        sap = new SAP(digraph);
        System.out.print("The path between 2 and 0 should be: [ 0 2 ] ");
        System.out.print("[");
        for (int i : sap.getPath(2, 0)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        List<Integer> one = new ArrayList<>(Arrays.asList(13, 23, 24));
        List<Integer> two = new ArrayList<>(Arrays.asList(6, 16, 17));
        System.out.println("==========================================================================================");
        System.out.println("The shortest common ancestor in above sets: " + sap.ancestor(one, two));
        System.out.println("==========================================================================================");
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

        System.out.print("The shortest path between 9 and 5 - in ambiguous-ancestor is [5, 6, 7, 8, 9]: ");
        System.out.print("[");
        // test 1 and 2 for ambiguous-ancestor
        for (int i : sap.getPath(9, 5)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

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

        digraph = new Digraph(new In(new File("src/main/resources/simplecycle.txt")));
        sap = new SAP(digraph);
        System.out.println("Expecting this to be true for simplecycle.txt: " + sap.hasCycle);
    }
}
