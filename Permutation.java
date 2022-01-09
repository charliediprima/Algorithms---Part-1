import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> bag = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);

        // Read in the strings and print k strings from the list at random
        while (!StdIn.isEmpty()) {
            bag.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(bag.sample());
        }
    }
 }