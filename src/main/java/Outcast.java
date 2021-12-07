import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Collections;

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
            distances[i]=dist;
            dist=0;
        }
        Arrays.sort(distances);
        return nouns[distances[nouns.length]];
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
