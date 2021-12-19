import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    // Given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        SAP sap = new SAP(wordNet.digraph);
        int[] distances = new int[nouns.length];
        int dist = 0;
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                dist += wordNet.distance(nouns[i], nouns[j]);
            }
            distances[i] = dist;
            dist = 0;
        }
        String result = "";
        int outcastIndex = 0;
        for (int i = 0; i < distances.length; i++) {
            StdOut.println("The distance for : " + nouns[i] + " is: " + distances[i]);
            if (distances[i] > outcastIndex) {
                result = nouns[i];
                outcastIndex = distances[i];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordNet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ":" + outcast.outcast(nouns));
        }
    }
}
