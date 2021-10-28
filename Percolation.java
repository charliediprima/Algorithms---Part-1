/* *****************************************************************************
 *  Name:    Charlie DiPrima
 *
 *  Description:  Models the percolation of an N x N grid 
 *
 *  Written:       10/27/2021
 *  Last updated:  10/27/2021
 *
 *  % javac -cp .:algs4/algs4.jar Percolation.java
 *  % java -cp .:algs4/algs4.jar Percolation [N]
 *  System percolated in [x] steps
 *
 **************************************************************************** */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private boolean [][] grid;
    private final WeightedQuickUnionUF algo;
    private int openSites = 0;
    private final int endNode, n;
    private boolean bottomRowFlag = false;

    // Creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {     
        if (n <= 0) {
            throw new IllegalArgumentException("N must be > 0");
        }

        grid = new boolean[n][n];
        this.n = n;
        algo = new WeightedQuickUnionUF(n * n + 2);
        endNode = n * n + 1;

        // Initialize all to false (closed site)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }

        // Connect dummy top node and dummy bottom node to their respective rows
        for (int i = 0; i < n; i++) {
            algo.union(0, i + 1);
            algo.union(endNode - i - 1, endNode);
        }
    }
    
    // Converts from the 2d array (grid) to the 1d array (used for algorithm)
    private int gridToAlgo(int row, int col) {
        return row * n + 1 + col;
    }

    // Opens the site (row, col) if it is not open already
    public void open(int row, int col) {      
        
        arrayCheck(row, col);
        // Accounts for grid starting at (1,1) convention
        row -= 1;
        col -= 1;

        if (!grid[row][col]) {
            // Performs union operation on any surrounding open cells
            if (row > 0) {
                if (grid[row - 1][col]) {
                    algo.union(gridToAlgo(row, col), gridToAlgo(row - 1, col));
                }
            }
        
            if (row < n - 1) {
                if (grid[row + 1][col]) {
                    algo.union(gridToAlgo(row, col), gridToAlgo(row + 1, col));
                }
            }

            if (col > 0) {
                if (grid[row][col - 1]) {
                    algo.union(gridToAlgo(row, col), gridToAlgo(row, col - 1));
                }
            }
        
            if (col < n - 1) {
                if (grid[row][col + 1]) {
                    algo.union(gridToAlgo(row, col), gridToAlgo(row, col + 1));
                }
            }

            grid[row][col] = true;
            openSites++;

            if (row == n - 1) {
                bottomRowFlag = true;
            }
        }
    }

    private void arrayCheck(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("Row and column values must be > 0");
        }
    } 

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        arrayCheck(row, col);
        if (grid[row - 1][col - 1]) {
            return true;
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        arrayCheck(row, col);
        if (algo.find(0) == algo.find(gridToAlgo(row - 1, col - 1)) && isOpen(row, col)) {
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }
    
    public boolean percolates() {
        if (algo.find(0) == algo.find(endNode) && bottomRowFlag) {
            return true;
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        
        Percolation perc = new Percolation(n);

        // Open random cells until the system percolates
        while (!perc.percolates()) {
            int randomRow = StdRandom.uniform(1, n + 1);
            int randomCol = StdRandom.uniform(1, n + 1);

            if (!perc.isOpen(randomRow, randomCol)) {
                perc.open(randomRow, randomCol);
            }
        }       
        
        System.out.printf("System percolated in %d steps%n", perc.openSites);
    }
}
