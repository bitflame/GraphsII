import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class Connected {
    boolean connected = true;

    public boolean isItConnected(Digraph digraph) {
// my way of checking if a digraph is a rooted DAG
        for (int i = 0; i < digraph.V(); i++) {
            DeluxBFS deluxBFS = new DeluxBFS(digraph, i);
            for (int v = 0; v < digraph.V(); v++) {
                connected = true;
                for (int j = 0; j < digraph.V(); j++) {
                    if (!deluxBFS.hasPathTo(j)) connected = false;
                }
                if (connected == true) break;
            }
        }
        return connected;
    }

    public Digraph createGraph(String hypernyms, int size) {
        In in = new In(hypernyms);
        Digraph digraph = new Digraph(size);/* assuming the number of ids are the same in synsets and hypernyms and I
        believe it is the number of vertices in the graph */
        int index = 0;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            for (int i = 0; i < a.length - 1; i++) {
                digraph.addEdge(Integer.parseInt(a[i]), Integer.parseInt(a[i + 1]));
            }
        }// check for cycles
        return digraph;
    }

    /*Here are other methods:
    from: https://www.coursera.org/learn/algorithms-part2/discussions/weeks/1/threads/zTY0b-amEeaVHA6A58f75A
    1) Ensure it is a DAG. Hint: https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Topological.html#hasOrder()
    1) Ensure there exists only one vertex R with outgoingDegree == 0.
    2) Ensure reachability from all other vertices to R. Hint: https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/BreadthFirstDirectedPaths.html#BreadthFirstDirectedPaths(edu.princeton.cs.algs4.Digraph,%20java.lang.Iterable)
    * */
    public boolean isRooted(Digraph digraph) {
        Topological topological = new Topological(digraph);
        int outDegreeCount = 0;
        int root = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) {
                outDegreeCount++;
                root = i;
            }
        }
        if (outDegreeCount == 1) {
            DeluxBFS deluxBFS = new DeluxBFS(digraph, root);
            for (int i = 0; i < digraph.V(); i++) {
                if (!deluxBFS.hasPathTo(i)) return false;
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Connected connected = new Connected();
        Digraph digraph = connected.createGraph("src/main/resources/hypernyms6InvalidCycle+Path.txt", 6);
        System.out.println(connected.isItConnected(digraph));
    }
}
