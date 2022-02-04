import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.Iterator;

public class SAP {
    private final Digraph digraphDFCopy;
    private int ancestor;
    private int minDistance;
    private int from;
    private int to;
    private int n;
    private boolean[] fromMarked;
    private boolean[] toMarked;
    private int[] edgeTo;
    private int[] fromDistTo;
    private int[] toDistTo;
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean print = false;

    // constructor takes a digraph ( not necessarily a DAG )
    public SAP(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("Digraph value can not be null");
        digraphDFCopy = new Digraph(digraph);
        n = digraphDFCopy.V();
        fromMarked = new boolean[n];
        toMarked = new boolean[n];
        edgeTo = new int[n];
        for (int i = 0; i < n; i++) {
            edgeTo[i] = -1;
        }
        fromDistTo = new int[n];
        toDistTo = new int[n];
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // System.out.println("length(): Calculating the distance between : " + v + " " + w);
        if (v < 0 || v >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (w < 0 || w >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (v == from && w == to && v != w) return minDistance;
        from = v;
        to = w;
        if (v == w) {
            ancestor = v;
            minDistance = 0;
            return 0;
        }
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to) == 0 &&
                digraphDFCopy.outdegree(to) == 0)) {
            ancestor = -1;
            return minDistance = -1;
        }
        if ((fromMarked[v] || toMarked[v]) && (fromMarked[w] || toMarked[w])) {
            updateAncestor(v, w);
        } else lockStepBFS(from, to);
        return minDistance;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.length() can not be null.\n");
        int currentDistance = 0;
        int prevDistance = INFINITY;
        //System.out.printf("sap triggers ancestor() with iterables ");
        Iterator<Integer> i = v.iterator();
        Iterator<Integer> j = w.iterator();
        if ((!i.hasNext()) || (!j.hasNext())) {
            return minDistance = -1;
        }
        int source;
        int destination;
        Object obj;
        while (i.hasNext()) {
            obj = i.next();
            if (obj == null)
                throw new IllegalArgumentException("The values Iterables give to length() can not be null.");
            else source = (Integer) obj;
            while (j.hasNext()) {
                obj = j.next();
                if (obj == null)
                    throw new IllegalArgumentException("The values Iterables give to length() can not be null.");
                destination = (Integer) obj;
                currentDistance = length(source, destination);
                // System.out.printf("Current Distance: %d \n", currentDistance);
                if (currentDistance != -1 && currentDistance < prevDistance) {
                    prevDistance = currentDistance;
                }
            }
            j = w.iterator();
        }
        // System.out.printf("Here is the last value in previous distance: %d\n" , prevDistance);
        if (prevDistance != INFINITY) minDistance = prevDistance;
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // System.out.println("Calculating the ancestor between : " + v + " " + w);
        if (v < 0 || v >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (w < 0 || w >= digraphDFCopy.V())
            throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (v < 0 || w < 0) throw new IllegalArgumentException("The node ids should be within acceptable range.\n");
        if (this.from == v && this.to == w && v != w) return ancestor;
        from = v;
        to = w;
        if (v == w) {
            return ancestor = v;
        }
        if ((digraphDFCopy.indegree(from) == 0 && digraphDFCopy.outdegree(from) == 0) || (digraphDFCopy.indegree(to) == 0 &&
                digraphDFCopy.outdegree(to) == 0)) {
            minDistance = -1;
            return ancestor = -1;
        }
        if ((fromMarked[v] || toMarked[v]) && (fromMarked[w] || toMarked[w])) {
            updateAncestor(v, w);
        } else lockStepBFS(from, to);
        return ancestor;
    }

    // method to find the ancestor if both source and destination are marked
    private void updateAncestor(int v, int w) {
        // 1- Find the new ancestor
        System.out.printf("inside updateAncestor()\n");
        int fromCount = 0, toCount = 0;
        int i = v, j = w;
        // Go from each side to the other and count the number of steps, then take the lower value. If you can not get to other side, run lockstepBFS
        // v to w
        while (i != -1 && i != w) {
            i = edgeTo[i];
            fromCount++;
            if (i == -1) break;
        }
        if (i != -1) {
            ancestor = v;
            minDistance = fromCount;
        }
        // w to v
        while (j != -1 && j != v) {
            j = edgeTo[j];
            toCount++;
            if (j == -1) break;
        }
        if (j != -1 && toCount < minDistance) {
            minDistance = toCount;
            ancestor = w;
        }
        if (i != w || j != v) {
            // run bfs
        }
        if (i == -1 && j == -1) {
            minDistance = -1;
            ancestor = -1;
        }
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Iterable value to SAP.ancestor() can not be null.\n");

        int len = 0;
        int prevLen = INFINITY;
        int currentAncestor = 0;
        Iterator<Integer> i = v.iterator();
        Iterator<Integer> j = w.iterator();
        if ((!i.hasNext()) || (!j.hasNext())) {
            return ancestor = -1;
        }
        int source;
        int destination;
        Object obj;
        while (i.hasNext()) {
            obj = i.next();
            if (obj == null)
                throw new IllegalArgumentException("The values Iterables give to length() can not be null.");
            else source = (Integer) obj;
            while (j.hasNext()) {
                obj = j.next();
                if (obj == null)
                    throw new IllegalArgumentException("The values Iterables give to length() can not be null.");
                destination = (Integer) obj;
                len = length(source, destination);
                if (len != -1 && len < prevLen) {
                    currentAncestor = ancestor;
                    prevLen = len;
                }
            }
            j = w.iterator();
        }
        ancestor = currentAncestor;
        minDistance = prevLen;
        return ancestor;
    }

    private void lockStepBFS(int f, int t) {
        /* todo - you can use digraph indegree to set edgeTo f and t maybe */
        fromMarked = new boolean[n];
        toMarked = new boolean[n];
        edgeTo = new int[n];
        for (int i = 0; i < n; i++) {
            edgeTo[i] = -1;
        }
        fromDistTo = new int[n];
        toDistTo = new int[n];
        Queue<Integer> fromQueue = new Queue<>();
        Queue<Integer> toQueue = new Queue<>();
        fromQueue.enqueue(f);
        toQueue.enqueue(t);
        fromMarked[f] = true;
        fromDistTo[f] = 0;
        toMarked[t] = true;
        toDistTo[t] = 0;
        int currentDistance = INFINITY;
        // System.out.printf("lockStepBFS(): Here is f: %d t: %d \n", f, t);

        while (!(fromQueue.isEmpty() && toQueue.isEmpty())) {
            if (!fromQueue.isEmpty()) {
                int v = fromQueue.dequeue();
                if (print) System.out.printf("took %d from fromQueue \n", v);
                for (int j : digraphDFCopy.adj(v)) {
                    if (fromMarked[j] && toMarked[j]) {
                        // still update the edgeTo and distanceTo j then calculate the minDistance etc
                        fromDistTo[j] = Math.min(fromDistTo[j], (fromDistTo[v] + 1));
                        int toDist = 0;
                        if (toDistTo[j] > 0) toDist = toDistTo[j];
                        edgeTo[j] = v;
                        int fromDist = 0;
                        if (fromDistTo[j] > 0) fromDist = fromDistTo[j];
                        if (print)
                            System.out.printf("Found an ancestor in the normal from match for f: %d t: %d ancestor: %d " +
                                    "fromDist = %d toDist = %d currentDist= %d \n", f, t, ancestor, fromDist, toDist, currentDistance);
                        if (currentDistance > (toDist + fromDist)) {
                            ancestor = j;
                            currentDistance = toDist + fromDist;
                            if (print)
                                System.out.printf("lockStepBfs(): updated the ancestor to %d and Current Distance to: " +
                                        "%d in the looped J block for f: %d, and t: %d\n", ancestor, currentDistance, f, t);
                        }
                    }
                    if (!fromMarked[j]) {
                        fromMarked[j] = true;
                        fromDistTo[j] = fromDistTo[v] + 1;
                        if (toMarked[j]) {
                            int toDist = 0;
                            if (toDistTo[j] > 0) toDist = toDistTo[j];
                            edgeTo[j] = v;
                            int fromDist = fromDistTo[j];
                            if (currentDistance > (toDist + fromDist)) {
                                ancestor = j;
                                currentDistance = toDist + fromDist;
                                if (print)
                                    System.out.printf("lockStepBfs(): updated the ancestor to %d and Current Distance to:" +
                                            " %d in the normal J block for f: %d, and t: %d\n", ancestor, currentDistance, f, t);
                            }
                        }
                        edgeTo[j] = v; // since it was not maked, add it to the queue to check its neighbors
                        fromQueue.enqueue(j);
                    }
                }
            }
            if (!toQueue.isEmpty()) {
                int w = toQueue.dequeue();
                if (print) System.out.printf("took %d from toQueue \n", w);
                for (int k : digraphDFCopy.adj(w)) {
                    if (fromMarked[k] && toMarked[k]) {
                        int fromDist = 0;
                        if (fromDistTo[k] > 0) fromDist = fromDistTo[k];
                        edgeTo[k] = w;
                        int toDist = 0;
                        if (toDistTo[k] > 0) toDist = Math.min(toDistTo[k], (toDistTo[w] + 1));
                        if (print)
                            System.out.printf("Found a potential ancestor in the looped to match for: from: %d, to: %d, " +
                                    "fromDist: %d, toDist: %d, currentDist: %d\n", f, t, fromDist, toDist, currentDistance);
                        if ((fromDist + toDist) < currentDistance) {
                            ancestor = k;
                            currentDistance = fromDist + toDist;
                            if (print)
                                System.out.printf("lockStepBfs(): updated the ancestor from the looped to %d and Minimum " +
                                        "Distance to: %d in K block for f: %d f: %d\n", ancestor, minDistance, f, t);

                        }
                    }
                    if (!toMarked[k]) {
                        toMarked[k] = true;
                        toDistTo[k] = toDistTo[w] + 1;
                        if (fromMarked[k]) {
                            int fromDist = 0;
                            if (fromDistTo[k] > 0) fromDist = fromDistTo[k];
                            edgeTo[k] = w;
                            int toDist = toDistTo[k];
                            if (currentDistance > (fromDist + toDist)) {
                                ancestor = k;
                                currentDistance = fromDist + toDist;
                                if (print)
                                    System.out.printf("lockStepBfs(): updated the ancestor to %d and Minimum Distance to: %d " +
                                            "in normal K block for f: %d f: %d\n", ancestor, minDistance, f, t);
                            }
                        }
                        if (print) System.out.printf("added %d to toQueue ", k);
                        edgeTo[k] = w;
                        toQueue.enqueue(k);
                    }
                }
            }
        }
        if (currentDistance == INFINITY) {
            // System.out.println("setting minDistance to -1 becuase currentDistance is INFINITY ");
            minDistance = -1;
            ancestor = -1;
            // return minDistance;
        } else minDistance = currentDistance;
        //return currentDistance;
    }

    // testEdgeTo(ancestor, x) and preAncestor'sNode like v, and w to the other end
    private boolean testEdgeTo(int ancestor, int destination) {
        //  System.out.printf("inside testEdge for " + from + " and " + to);
        if (ancestor == destination) return true;
        int i = ancestor;
        int counter = 0;
        for (; i != destination && counter < n; i = edgeTo[i]) {
            if (i == -1) break;
            counter++;
            //System.out.print(" " + i);
        }
        // if (i != -1) System.out.print(" " + i);

        return (i == destination);
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In("digraph3.txt"));
        SAP sap = new SAP(digraph);
        System.out.printf("%b\n", sap.testEdgeTo(11, 12));
        System.out.printf("%b\n", sap.testEdgeTo(11, 8));
    }
}
