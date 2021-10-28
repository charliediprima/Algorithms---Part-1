import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        double p = 1.0;
        String word = "Please enter a series of words seperated by spaces";
        String candidate;
        
        while (!StdIn.isEmpty()) {
            candidate = StdIn.readString();
            
            if (StdRandom.bernoulli(1/p)) {
                word = candidate;
            }
            p++;
        }
        StdOut.println(word);
    }
}
