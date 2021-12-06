import edu.princeton.cs.algs4.*;

public class WordNet {
    Queue<String> nouns;
    BST<Integer, Bag<String>> ST;
    int size;
    boolean hasCycle = false;

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
                    Bag b = ST.get(index);
                    b.add(s);
                    ST.put(index, b);
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
            // index is the id
            for (int i = 0; i < a.length - 1; i++) {
                digraph.addEdge(Integer.parseInt(a[i]), Integer.parseInt(a[i + 1]));
            }
        }// check for cycles
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            return;
        }
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
        WordNet wordNet = new WordNet(args[0], args[1]);
    }
}
