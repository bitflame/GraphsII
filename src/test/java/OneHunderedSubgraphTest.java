public class OneHunderedSubgraphTest {
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        int dis = wordNet.distance("physical_entity", "thyroglobulin");
        // System.out.printf("The distance between physical_entity and thyroglobulin: %d \n" , dis);
        if (dis != 15)
            throw new AssertionError("The distance between Tallchief, and B-52 should be 15, but it actually is: " + dis);

    }
}
