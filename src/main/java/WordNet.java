import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class WordNet {
    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);
        int index;
        String synset;
        System.out.printf("Index------Synset\n");
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            index = Integer.parseInt(a[0]);
            System.out.print("   "+index+"         ");
            synset = a[1];
            String[] syns = synset.split(" ");
            for (String s:syns) {
                System.out.print(s);
            }
            System.out.println();
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Queue<String> nouns = new Queue<>();
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return true; // for now
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
