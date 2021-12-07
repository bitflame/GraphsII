import edu.princeton.cs.algs4.*;

public class WordNet {
    Queue<String> nouns;
    BST<Integer, Bag<String>> ST;
    int size;
    boolean hasCycle = false;
    boolean connected = true;

    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {
        createDb(synsets);
        createGraph(hypernyms);
    }

    private void createDb(String synsets) {
        In in = new In(synsets);
        int index = 0;
        String synset;
        nouns = new Queue<>();
        ST = new BST<>();
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            index = Integer.parseInt(a[0]);
            synset = a[1];
            String[] syns = synset.split(" ");
            for (String s : syns) {
                nouns.enqueue(s);
                if (ST.contains(index)) {
                    ST.get(index).add(s);
                } else {
                    Bag b = new Bag();
                    b.add(s);
                    ST.put(index, b);
                }
            }
        }
        size = ST.size();
    }

    private void createGraph(String hypernyms) {
        In in = new In(hypernyms);
        Digraph digraph = new Digraph(size);/* assuming the number of ids are the same in synsets and hypernyms and I
        believe it is the number of vertices in the graph */
        int index = 0;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            if (a.length > 2) throw new IllegalArgumentException("each vertex should have one root or parent");
            for (int i = 0; i < a.length - 1; i++) {
                digraph.addEdge(Integer.parseInt(a[i]), Integer.parseInt(a[i + 1]));
            }
        }// check for cycles
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
        if (!isConnected(digraph)) throw new IllegalArgumentException("The input data is not for a connected graph");
    }
    private boolean isConnected(Digraph digraph) {

        for (int i = 0; i < digraph.V(); i++) {
            DeluxBFS deluxBFS = new DeluxBFS(digraph, i);
            for (int v = 0; v < digraph.V(); v++) {
                connected=true;
                for (int j : digraph.adj(v)) {
                    if (!deluxBFS.hasPathTo(j)) connected = false;
                }
                if (connected==true) break;
            }
        }
        return connected;
    }
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        for (int i : ST.keys()) {
            for (String s : ST.get(i)) {
                if (s.equals(word)) return true;
            }
        }
        return false;
    }

    // distance between nounA and nounB (defined below )
    public int distance(String nounA, String nounB) {
        return -1; // for now
    }

    /* a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral
     * path (defined below) */
    public String sap(String nounA, String nounB) {
        return "";// for now
    }

    // do unit testing here
    public static void main(String[] args) {
        System.out.println("using " + args[0] + "and " + args[1] + "files for this round.");
        WordNet wordNet = new WordNet(args[0], args[1]);
    }
}
