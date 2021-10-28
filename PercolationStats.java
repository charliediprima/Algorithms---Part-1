import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final double[] percThreshold;
    private double mean, stddev, confidenceLo, confidenceHi;
    private final int trials;

    // Perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N grid length and T trials must both be > 0");
        }
        
        this.trials = trials;
        percThreshold = new double[trials];

        // Perform all trials, recording the percolation threshold for each
        for (int i = 0; i < trials; i++) {  
            int openSites = 0;
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int randomRow = StdRandom.uniform(1, n + 1);
                int randomCol = StdRandom.uniform(1, n + 1);
    
                if (!perc.isOpen(randomRow, randomCol)) {
                    perc.open(randomRow, randomCol);
                    openSites++;
                }
            }
            percThreshold[i] = ((double) openSites / (double) (n * n));
        }
    }

    // Sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percThreshold);
    }

    // Sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percThreshold);
    }

    // Low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials)));
    }

    // High endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials)));
    }

    // test client (see below)
    public static void main(String[] args) {

        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        
        stats.mean = stats.mean();
        stats.stddev = stats.stddev();
        stats.confidenceLo = stats.confidenceLo();
        stats.confidenceHi = stats.confidenceHi();

        System.out.printf("mean                    = %f%n", stats.mean);
        System.out.printf("stddev                  = %f%n", stats.stddev);        
        System.out.printf("95%% confidence interval = [%f, %f]%n", stats.confidenceLo, stats.confidenceHi);
    }
}
