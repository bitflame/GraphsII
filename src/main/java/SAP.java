import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAP {
    boolean hasCycle;
    private Digraph digraph;


    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
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
        List<List<Integer>> paths = new ArrayList<>();
        List<Integer> singlePath = new ArrayList<>();
        for (int i : v) {
            for (int j : w) {
                singlePath = getPath(i, j);
            }
            paths.add(singlePath);
        }
        for (Object obj : paths) {
            ArrayList<Integer> temp = (ArrayList<Integer>) obj;
            if (temp.size() < singlePath.size()) singlePath = temp;
        }
        return singlePath.size();
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // Note - this won't work if the first match is not the shortest path but not other info is in Iterable
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
        //System.out.println("Here is everything in pathToTo");
        for (int v = digraph.V() - 1; v >= 0; v--) {
            if (pathToTo.hasPathTo(v) && !pathToFrom.hasPathTo(v)) {
                shortestPath.add(v);
            }
            if (!pathToTo.hasPathTo(v) && pathToFrom.hasPathTo(v)) {
                shortestPath.add(v);
            }
            if (pathToFrom.hasPathTo(v) && pathToTo.hasPathTo(v)) {
                shortestPath.add(v);
                return shortestPath;
            }
        }
        return shortestPath;
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

        System.out.println("]");
        System.out.println();
        System.out.print("The path between 23 and 24 should be: [ 20 ] ");
        System.out.print("[");
        for (int i : sap.getPath(23, 24)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        System.out.println("]");
        System.out.println();
        System.out.print("The path between 11 and 4 should be: [  0 1 2 5] ");
        System.out.print("[");
        for (int i : sap.getPath(11, 4)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

        System.out.println("]");
        System.out.println();
        System.out.print("The path between 17 and 19 should be: [ 5 10 12] ");
        System.out.print("[");
        for (int i : sap.getPath(17, 19)) {
            System.out.print(" " + i + " ");
        }
        System.out.println("]");
        System.out.println();

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
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }

    }
}
