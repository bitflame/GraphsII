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

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return getPath(v, w).size();
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

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        shortestPath(v, w);
        return ancestor;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // Note - this won't work if the first match is not the shortest path but not other info is in Iterable
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.");
        for (int i : v) {
            for (int j : w) {
                shortestPath(i, j);
            }
        }
        return ancestor;
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
        if (from == to) {
            sPath.add(from);
            return sPath;
        }
        /* Build a min priority queue for to and from based on the node's distance to each node, then use A*:-) to
         * process nodes */

        List<Integer> sources = new ArrayList<>();
        sources.add(from);
        sources.add(to);
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
        /* The last value might also be tDBS.distTo(from) todo: check the numbers tomorrow */
        Node fromNode = new Node(from, null, 0, tDBS.distTo(from));
        fromQueue.insert(fromNode);
        Node toNode = new Node(to, null, 0, fDBS.distTo(to));
        toQueue.insert(toNode);
        Node minFNode = fromQueue.delMin();
        Node minTNode = toQueue.delMin();
        for (int i : digraph.adj(minTNode.id)) {
            Node newNode = new Node(i, minTNode, minTNode.movesTaken + 1, fDBS.distTo(i));
            toQueue.insert(newNode);
        }
        for (int i : digraph.adj(minFNode.id)) {
            Node newNode = new Node(i, minFNode, minFNode.movesTaken + 1, tDBS.distTo(i));
            fromQueue.insert(newNode);
        }
        minFNode = fromQueue.delMin();
        while (minFNode.id != minTNode.id) {

            for (int i : digraph.adj(minFNode.id)) {
                if (i != minFNode.prevNode.id) { // to address A*'s problem with the node before parent
                    Node newNode = new Node(i, minFNode, minFNode.movesTaken + 1, tDBS.distTo(i));
                    fromQueue.insert(newNode);
                }
            }
            if (!fromQueue.isEmpty()) minFNode = fromQueue.delMin();
            if (minFNode.id == minTNode.id) break;
            if (!toQueue.isEmpty()) minTNode = toQueue.delMin();
            if (minFNode.id == minTNode.id) break;
            for (int i : digraph.adj(minTNode.id)) {
                Node newNode = new Node(i, minTNode, minTNode.movesTaken + 1, fDBS.distTo(i));
                toQueue.insert(newNode);
            }
            if (!toQueue.isEmpty()) minTNode = toQueue.delMin();
            if (minFNode.id == minTNode.id) break;
        }
        while(minTNode.prevNode!=null && minFNode.prevNode.id==minTNode.prevNode.id) {
            minFNode=minFNode.prevNode;
            minTNode=minTNode.prevNode;
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

    public Iterable<Integer> shortPath(int from, int to) {
        List<Integer> thePath = new ArrayList<>();
        DeluxBFS currentSearch;
        List<Integer> s;
        List<Integer> tempPath = new ArrayList<>();
        for (int i = 0; i < digraph.V(); i++) {
            s = new ArrayList<>(Arrays.asList(from, to, i));
            currentSearch = new DeluxBFS(digraph, s);
            for (int j = 0; j < digraph.V(); j++) {
                if (currentSearch.hasPathTo(j))
                    thePath.add(j);
            }
            if (!tempPath.isEmpty() && tempPath.size() > thePath.size() && thePath.contains(Arrays.asList(from, to))) {
                tempPath = thePath;

            } else if (tempPath.isEmpty()) {
                tempPath = thePath;

            }
            thePath = new ArrayList<>();
        }
        return tempPath;
    }

    public static void main(String[] args) {
        /*Digraph digraph = new Digraph(new In(new File("src/main/resources/digraph-ambiguous-ancestor.txt")));
        SAP sap = new SAP(digraph);
        System.out.println(sap.ancestor(1, 2));
        System.out.println(sap.ancestor(0, 2));
        System.out.println(sap.ancestor(0, 1));
        System.out.println(sap.ancestor(0, 10));
        digraph = new Digraph(new In(new File("src/main/resources/digraph25.txt")));
        sap = new SAP(digraph);
        sap.ancestor(19, 24);
        sap.ancestor(1, 2);
        sap.ancestor(0, 24);*/
        Digraph digraph = new Digraph(new In(args[0]));
        SAP sap = new SAP(digraph);
        System.out.print("Using shortPath() - The path between 4 and 6 should be [0 1 2 4 6] : ");
        System.out.print("[");
        for (int i : sap.shortestPath(4, 6)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        System.out.print("Using A* in shortestPath() - The path between 1 and 2 should be [0 1 2] : ");
        System.out.print("[");
        for (int i : sap.shortestPath(1, 2)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();
        System.out.print("Using A* in shortestPath() - The path between 1 and 2 should be [0 1 2] : ");
        System.out.print("[");
        for (int i : sap.shortestPath(3, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        sap.shortestPath(1, 2);
        sap.shortestPath(3, 4);
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
