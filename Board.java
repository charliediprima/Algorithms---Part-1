import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private final int[][] goalBoard;
    private final int[][] tiles;
    private final int n;
    private Board twinBoard;
    private boolean twinFlag = false;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // Initialize instance variable tiles and generate goal board
        this.n = tiles[0].length;
        this.goalBoard = new int[n][n];
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.goalBoard[i][j] = i * n + j + 1;
                this.tiles[i][j] = tiles[i][j];
            }
        }
        this.goalBoard[n-1][n-1] = 0;
    }     

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != goalBoard[i][j]) {
                    if (tiles[i][j] != 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int squares = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((tiles[i][j] != goalBoard[i][j]) && tiles[i][j] != 0) {
                    // Compute the distance (number of squares) from the current location to the goal
                    int value = tiles[i][j] - 1;
                    int iGoal = value / n;
                    int jGoal = value % n;
                    squares += Math.abs(iGoal - i) + Math.abs(jGoal - j);
                }
            }
        }
        return squares;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return Arrays.deepEquals(tiles, goalBoard);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        return Arrays.deepEquals(that.tiles, this.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighborBoards = new Stack<Board>();
        
        // Initialize row and col to -1 for error checking purposes
        int row = -1, col = -1;

        // Locates the blank square
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }

        boolean leftSwap = false;
        boolean rightSwap = false;
        boolean upSwap = false;
        boolean downSwap = false;

        // Check to verify blank square has been found/exists
        if (row != -1 && col != -1) {
            
            // Determine which swaps have to be performed depending upon corner conditions
            if (row == 0 && col == 0) {
                rightSwap = true;
                downSwap = true;
            }

            else if (row == 0 && col == n - 1) {
                leftSwap = true;
                downSwap = true;
            }

            else if (row == n - 1 && col == 0) {
                rightSwap = true;
                upSwap = true;
            }

            else if (row == n - 1 && col == n - 1) {
                leftSwap = true;
                upSwap = true;
            }

            else if (row == 0) {
                leftSwap = true;
                rightSwap = true;
                downSwap = true;
            }

            else if (row == n - 1) {
                leftSwap = true;
                rightSwap = true;
                upSwap = true;
            }

            else if (col == 0) {
                rightSwap = true;
                downSwap = true;
                upSwap = true;
            }

            else if (col == n - 1) {
                leftSwap = true;
                downSwap = true;
                upSwap = true;
            }

            else {
                leftSwap = true;
                rightSwap = true;
                downSwap = true;
                upSwap = true;
            }

            // Generate all neighboring boards by performing the requisite swaps
            if (rightSwap) {
                Board addBoard = new Board(tiles);
                addBoard.swap(addBoard.tiles, row, col, row, col + 1);
                neighborBoards.push(addBoard);
            }

            if (leftSwap) {
                Board addBoard = new Board(tiles);
                addBoard.swap(addBoard.tiles, row, col, row, col - 1);
                neighborBoards.push(addBoard);
            }

            if (downSwap) {
                Board addBoard = new Board(tiles);
                addBoard.swap(addBoard.tiles, row, col, row + 1, col);
                neighborBoards.push(addBoard);
            }

            if (upSwap) {
                Board addBoard = new Board(tiles);
                addBoard.swap(addBoard.tiles, row, col, row - 1, col);
                neighborBoards.push(addBoard);
            }
        }
        return neighborBoards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // Create new board and corresponding array
        if (twinFlag) {
            return this.twinBoard;
        }

        else {
            int[][] twin = new int[n][n];

            // Make a copy of the board array
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    twin[i][j] = tiles[i][j];
                }
            }
    
            // Swap the tiles
            while (true) {
                int a = StdRandom.uniform(n);
                int b = StdRandom.uniform(n);
                int c = StdRandom.uniform(n);
                int d = StdRandom.uniform(n);
                if ((twin[a][b] != 0 && twin[c][d] != 0) && (a != c || b != d)) {
                    swap(twin, a, b, c, d);
                    break;
                }
            }
            twinFlag = true;
            this.twinBoard = new Board(twin);
            return this.twinBoard;
        }
    }

    // Swaps two tiles - a/b and c/d correspond to row/col
    private int[][] swap(int[][] t, int a, int b, int c, int d) {
        int temp = t[a][b];
        t[a][b] = t[c][d];
        t[c][d] = temp;
        return t;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{1, 7, 3}, {4, 2, 5}, {0, 8, 6}};
        int[][] tiles2 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board test = new Board(tiles);
        Board test2 = new Board(tiles2);
        StdOut.println(test.toString());
        StdOut.println(test.dimension());
        StdOut.println(test.hamming());
        StdOut.println(test.manhattan());
        StdOut.println(test.twin());
        StdOut.println(test.neighbors());
        StdOut.println(test.toString());
        StdOut.println(test2.toString());
        StdOut.println(test.equals(test2));
    }
}