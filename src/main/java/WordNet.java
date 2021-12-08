import edu.princeton.cs.algs4.*;

public class WordNet {
    RedBlackBST<Integer, String> st;
    Queue<String> nouns;
    int size = 0;  // number of nouns
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
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            val = Integer.parseInt(a[0]);
            String[] syns = a[1].split(" ");
            st.put(val, a[1]);
            for (String noun : syns) {
                nouns.enqueue(noun);
            }
            size++;
        }
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
        int idOfA = -1;
        int idOfB = -1;
        /*
        while (idOfA != idOfB && st.keys().iterator().hasNext()){
            String s = st.get(st.keys().iterator().next());
            if (s.equals(nounA)) idOfA=
        }
        I need to find the id that matches worm with bird
        */
        for (int i : st.keys()) {
            if (st.get(i).equals(nounA)) {
                idOfA = i;
            } else {
                for (String s : st.get(i).split(" ")) {
                    if (s.equals(nounA)) idOfA = i;
                }
            }
            if (st.get(i).equals(nounB)) idOfB = i;
            else {
                for (String s : st.get(i).split(" ")) {
                    if (s.equals(nounB)) idOfB = i;
                }
            }
            if (idOfA == idOfB) break;
        }
        if (idOfA >= 0 && idOfB >= 0) {
            SAP sap = new SAP(digraph);
            return sap.length(idOfA, idOfB);
        }
        return -1;// if the nouns are not in the db
    }

    /* a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral
     * path (defined below) */
    public String sap(String nounA, String nounB) {
        StringBuilder sb = new StringBuilder();
        int idOfA = -1;
        int idOfB = -1;
        for (int i : st.keys()) {
            if (st.get(i).equals(nounA)) idOfA = i;
            else {
                for (String s : st.get(i).split(" ")) {
                    if (s.equals(nounA)) idOfA = i;
                }
            }
            if (st.get(i).equals(nounB)) idOfB = i;
            else {
                for (String s : st.get(i).split(" ")) {
                    if (s.equals(nounB)) idOfB = i;
                }
            }
        }
        if (idOfA >= 0 && idOfB >= 0) {
            SAP sap = new SAP(digraph);
            for (int i : sap.getPath(idOfA, idOfB)) {
                sb.append(st.get(i));
            }
        }
        return sb.toString();
    }

    // do unit testing here
    public static void main(String[] args) {
        System.out.println("using " + args[0] + " and " + args[1] + "files for this round.");
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.isNoun("entity"));
        System.out.println(wordNet.sap("worm", "bird"));
        System.out.println(wordNet.distance("worm", "bird"));
    }
}
