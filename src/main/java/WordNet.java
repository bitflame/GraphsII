import edu.princeton.cs.algs4.*;

public class WordNet {
    Queue<String> nouns;
    BST<String, Integer> st;
    private String[] keys;
    int size=0;  // number of nouns
    boolean hasCycle = false;
    boolean rooted = true;

    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {
        createDb(synsets);
        createGraph(hypernyms);
    }

    private void createDb(String synsets) {
        In in = new In(synsets);
        int val;
        nouns = new Queue<>();
        st = new BST<>();
        while (in.hasNextLine()) {
            size++;
            String[] a = in.readLine().split(",");
            val = Integer.parseInt(a[0]);
            String[] syns = a[1].split(" ");
            for (String key : syns) {
                nouns.enqueue(key);
                st.put(key,val);
            }
        }
    }

    private void createGraph(String hypernyms) {
        In in = new In(hypernyms);
        Digraph digraph = new Digraph(size);
        int index = 0;
        while (in.hasNextLine()) {
            index++;
            String[] a = in.readLine().split(",");
            for (int i = 0; i < a.length - 1; i++) {
                digraph.addEdge(Integer.parseInt(a[i]), Integer.parseInt(a[i + 1]));
            }
        }// check for cycles
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG");
        }
        if (index >= size) {
            rooted = false;
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return st.get(word)!=null;
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
