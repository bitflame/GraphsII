import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.List;

public class Topological {
    private Iterable<Integer> order;

    public Topological(Digraph G) {
        DirectedCycle cyclefinder = new DirectedCycle(G);
        if (!cyclefinder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        }
    }

    public Iterable<Integer> order() {
        return order;
    }

    public boolean isDAG() {
        return order == null;
    }
// I got this code from https://github.com/reneargento/algorithms-sedgewick-wayne/blob/master/src/chapter4/section2/Exercise9.java
    private boolean isTopologicalOrder(Digraph G, List<Integer> topologicalOrder) {
        DirectedCycle cyclefinder = new DirectedCycle(G);
        if (cyclefinder.hasCycle()) return false;
        if (topologicalOrder.size() != G.V()) return false;
        boolean[] visited = new boolean[G.V()];
        for (int i = topologicalOrder.size() - 1; i >= 0; i--) {
            int vertex = topologicalOrder.get(i);
            if (visited[vertex]) {
                return false;
            }
            visited[vertex] = true;
            for (int neighbor : G.adj(vertex)) {
                if (!visited[neighbor]){
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String filename = args[0];
        String separator = args[1];
        SymbolDigraph sg = new SymbolDigraph(filename, separator);
        Topological top = new Topological(sg.digraph());
        for (int v : top.order())
            StdOut.println(sg.nameOf(v));
        Digraph digraph = new Digraph(3);
        digraph.addEdge(0,1);
        digraph.addEdge(0,2);
        digraph.addEdge(1,2);
        List<Integer> topologicalOrder = new ArrayList<>();
        topologicalOrder.add(0);
        topologicalOrder.add(1);
        topologicalOrder.add(2);
        boolean isTopOrder = top.isTopologicalOrder(digraph,topologicalOrder);
        StdOut.println("Is topological order: "+ isTopOrder + " Expected: true");
    }
}
