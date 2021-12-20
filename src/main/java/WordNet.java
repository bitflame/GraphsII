import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.List;

public class WordNet {
    RedBlackBST<Integer, String> st;
    Queue<String> nouns;
    int size = 0;  // number of synsets
    int nounCounter = 0;
    boolean hasCycle = false;
    boolean rooted = true;
    Digraph digraph;

    // SAP sap; use this when you are ready to improve the performance and other unit tests pass
    // constructor takes the name of two input files
    public WordNet(String synsets, String hypernyms) {
        createDb(synsets);
        createGraph(hypernyms);
    }

    private void createDb(String synsets) {
        In in = new In(synsets);
        int val;
        st = new RedBlackBST<>();
        nouns = new Queue<>();
        String prevString = null;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            val = Integer.parseInt(a[0]);
            String[] syns = a[1].split(" ");
            st.put(val, a[1]);
            for (String noun : syns) {
                // if noun is equal to prevString or part of it, do not add it todo - test to make sure this works
                if (prevString == null || !prevString.contains(noun)) {
                    nouns.enqueue(noun);
                    nounCounter++; // this is a lot more than 119,188 b/c of redundant nouns
                }
                prevString = a[1];
            }
            size++;
        }
        /*StdOut.println("worm ids right after st creation: ");
        for (int i:st.keys()) {
            for(String s: st.get(i).split(" ")){
                if (s.equals("worm")) StdOut.println(i+" ");
            }
        }
        StdOut.println();*/
    }

    private void createGraph(String hypernyms) {
        In in = new In(hypernyms);
        digraph = new Digraph(size);
        int index = 0;
        while (in.hasNextLine()) {
            index++;
            String[] a = in.readLine().split(",");
            for (int i = 0; i < a.length - 1; i++) {
                digraph.addEdge(Integer.parseInt(a[0]), Integer.parseInt(a[i + 1]));
            }
        }// check for cycles
        DirectedCycle cycleFinder = new DirectedCycle(digraph);
        if (cycleFinder.hasCycle()) {
            hasCycle = true;
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - cycle detected");
        }
        if (Math.abs(index - size) > 1) {
            rooted = false;
            throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG - Graph Not rooted");
        }
        /*StdOut.println("worm ids right after graph creation: ");
        for (int i:st.keys()) {
            for(String s: st.get(i).split(" ")){
                if (s.equals("worm")) StdOut.print(i+" ");
            }
        }*/
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        for (int i : st.keys()) {
            if (st.get(i).equals(word)) return true;
            for (String s : st.get(i).split(" ")) {
                if (s.equals(word)) return true;
            }
        }
        return false;
    }

    // distance between nounA and nounB (defined below )
    public int distance(String nounA, String nounB) {
        List<Integer> idsOfA = new ArrayList<>();
        List<Integer> idsOfB = new ArrayList<>();
        for (int k : st.keys()) {
            for (String s : st.get(k).split(" ")) {
                if (s.equals(nounA)) idsOfA.add(k);
                if (s.equals(nounB)) idsOfB.add(k);
            }
        }
        SAP sap = new SAP(digraph);

        return sap.length(idsOfA, idsOfB);// if the nouns are not in the db
    }

    /* a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral
     * path (defined below) */
    public String sap(String nounA, String nounB) {
        List<Integer> idsOfA = new ArrayList<>();
        List<Integer> idsOfB = new ArrayList<>();
        for (int k : st.keys()) {
            for (String s : st.get(k).split(" ")) {
                if (s.equals(nounA)) idsOfA.add(k);
                if (s.equals(nounB)) idsOfB.add(k);
            }
        }
       /* StdOut.printf("Here are the ids for first term: \n");
        for (int i : idsOfA) {
            System.out.println(" " + i);
        }
        StdOut.printf("Here are the ids for the secnd term: \n");
        for (int i : idsOfB) {
            System.out.println(" " + i);
        }*/
        SAP sap = new SAP(digraph);
        int result = sap.ancestor(idsOfA, idsOfB);
        //StdOut.println("Here is the id of the ancestor returned by SAP: " + result);
        return st.get(result);
    }

    private void testSap(int i, int j) {
        SAP sap = new SAP(digraph);
        StdOut.println(st.get(sap.ancestor(i, j)) + " with the length of:   " + sap.length(i, j));
        StdOut.println("The path for " + i + " and " + j + " is: ");
        /*
        for (int k:sap.getPath(i,j)) {
            StdOut.print(k+" ");
        }
        */
    }

    // do unit testing here
    public static void main(String[] args) {
        System.out.println("using " + args[0] + " and " + args[1] + "files for this round.");
        WordNet wordNet = new WordNet(args[0], args[1]);
        Stopwatch time = new Stopwatch();
        System.out.println("The common ancestor between worm and animal: " + wordNet.sap("worm", "bird"));
        Double now = time.elapsedTime();
        StdOut.println("worm/bird test using HashTables took: "+now);
        System.out.println(wordNet.isNoun("entity"));
        //System.out.println("The common ancestor for worm and bird : " + wordNet.sap("worm", "bird"));
        //System.out.println("The distance expected between worm and bird is 5, the result: " +wordNet.distance("worm", "bird"));
        StdOut.println("The common ancestor for quadrangle and mountain_devil is:" +
                wordNet.sap("quadrangle", "mountain_devil"));
        StdOut.println("The distance expected between mountain_devil and quadrangle should be 11, the result: " +
                wordNet.distance("quadrangle", "mountain_devil"));
        /* System.out.println("************************* Testing wordnet ancestor and distance with ids *****************");
        wordNet.testSap(81679, 24306);
        wordNet.testSap(81679, 24307);
        wordNet.testSap(81679, 25293);
        wordNet.testSap(81679, 33764);
        wordNet.testSap(81679, 70067);
        wordNet.testSap(81680, 24306);
        wordNet.testSap(81680, 24307);
        wordNet.testSap(81680, 25293);
        wordNet.testSap(81680, 33764);
        wordNet.testSap(81680, 70067);
        wordNet.testSap(81681, 24306);
        wordNet.testSap(81681, 24307);
        wordNet.testSap(81681, 25293);
        wordNet.testSap(81681, 33764);
        wordNet.testSap(81681, 70067);
        wordNet.testSap(81682, 24306);
        wordNet.testSap(81682, 24307);
        wordNet.testSap(81682, 25293);
        wordNet.testSap(81682, 33764);
        wordNet.testSap(81682, 70067); */
    }
}
