import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

public class Misc {

RedBlackBST<String,Integer> db;
    public void createDb(String synset) {
        db = new RedBlackBST<>();
        In in = new In(synset);
        while (in.hasNextLine()){
            String[] a = in.readLine().split(",");
            db.put(a[1],Integer.parseInt(a[0]));
        }
    }

    public static void main(String[] args) {
        Misc misc = new Misc();
        misc.createDb(args[0]);
    }
}
