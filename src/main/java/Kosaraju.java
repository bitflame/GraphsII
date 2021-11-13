import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Kosaraju {
    private boolean[] marked;
    private int[] id;
    private int count;

    public Kosaraju(Digraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder order = new DepthFirstOrder(G.reverse());
        for (int s : order.reversePost())
            if (!marked[s]) {
                dfs(G, s);
                count++;
            }
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }

    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

    public int id(int v) {
        return id[v];
    }

    public int count() {
        return count;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));
        Kosaraju kr = new Kosaraju(digraph);
        StdOut.println(kr.count+" componenets.");
        for (int i: kr.id) StdOut.println(i);
    }
}
