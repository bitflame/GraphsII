public class WordNetTests {
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        // System.out.println("The distance between Tallchief, and B-52 should be 15, and it is: " + wordNet.distance("Tallchief", "B-52"));
        if (wordNet.distance("Tallchief", "B-52")!= 15)
            throw new AssertionError("The distance between Tallchief, and B-52 should be 15");
    }
}
