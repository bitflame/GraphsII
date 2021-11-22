import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;

public class TransitiveClosure {
    DirectedDFS[] all;

    TransitiveClosure(Digraph digraph) {
        all = new DirectedDFS[digraph.V()];
        for (int v = 0; v < digraph.V(); v++) {
            all[v] = new DirectedDFS(digraph, v);
        }
    }

    public boolean reachable(int v, int w) {
        return all[v].marked(w);
    }
}
