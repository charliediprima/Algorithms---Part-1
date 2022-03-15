import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private class Node implements Comparable<Node> {
        Board board;
        int moves, manhattan, priority;
        Node previous;

        public int compareTo(Node that) {
            if (this.priority > that.priority) {
                return 1;
            }

            else if (this.priority < that.priority) {
                return -1;
            }

            else {
                if (this.manhattan > that.manhattan) {
                    return 1;
                }

                else if (this.manhattan < that.manhattan) {
                    return -1;
                }

                else {
                    return 0;
                }
            }

        }
    }
    
    // p___ refers to primary operations, t___refers to the twin operations used to check whether the board is solvable
    private Node finalNode;
    private final int solutionMoves;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("No argument to Solver constructor");
        }
        final MinPQ<Node> pQueue = new MinPQ<Node>();
        final MinPQ<Node> tQueue = new MinPQ<Node>();
        final Board pInitial, tInitial;

        // Add the first node (corresponds to the initial board) to the PQ
        pInitial = initial;
        tInitial = initial.twin();
        Node pNode = new Node();
        Node tNode = new Node();
        pNode.board = pInitial;
        tNode.board = tInitial;
        pNode.moves = 0;
        tNode.moves = 0;
        pNode.manhattan = pNode.board.manhattan();
        tNode.manhattan = tNode.board.manhattan();
        pNode.priority = pNode.manhattan + pNode.moves;
        tNode.priority = tNode.manhattan + tNode.moves;
        pNode.previous = null;
        tNode.previous = null;
        pQueue.insert(pNode);
        tQueue.insert(tNode);

        int i = 0;

        // Perform A* search algorithm until it is solved (goal board found)
        while (!pNode.board.isGoal() && !tNode.board.isGoal()) {
            for (Board b : pNode.board.neighbors()) {
                // Avoid referencing null previous board on first iteration
                if (i == 0) {
                    Node n = new Node();
                    n.board = b;
                    n.manhattan = b.manhattan();
                    n.moves = i;
                    n.priority = n.manhattan + n.moves;
                    n.previous = pNode;
                    pQueue.insert(n);
                }
                // Iterate through adjacent boards and add them to the PQ
                else {
                    // Critical Optimization: Detect whether the board is a repeat of a previous search node
                    if (!b.equals(pNode.previous.board)) {
                        Node n = new Node();
                        n.board = b;
                        n.manhattan = b.manhattan();
                        n.moves = i;
                        n.priority = n.manhattan + n.moves;
                        n.previous = pNode;
                        pQueue.insert(n);
                    }
                }
            }

            for (Board b : tNode.board.neighbors()) {
                // Avoid referencing null previous board on first iteration
                if (i == 0) {
                    Node n = new Node();
                    n.board = b;
                    n.manhattan = b.manhattan();
                    n.moves = i;
                    n.priority = n.manhattan + n.moves;
                    n.previous = tNode;
                    tQueue.insert(n);
                }
                // Iterate through adjacent boards and add them to the PQ
                else {
                    // Critical Optimization: Detect whether the board is a repeat of a previous search node
                    if (!b.equals(tNode.previous.board)) {
                        Node n = new Node();
                        n.board = b;
                        n.manhattan = b.manhattan();
                        n.moves = i;
                        n.priority = n.manhattan + n.moves;
                        n.previous = tNode;
                        tQueue.insert(n);
                    }
                }
            }
            // Remove the lowest priority node from the PQ
            pNode = pQueue.delMin();
            tNode = tQueue.delMin();
            i++;
        }

        solutionMoves = i;
        if (pNode.board.isGoal()) {
            finalNode = pNode;
            solvable = true;
        }

        else if (tNode.board.isGoal()) {
            finalNode = tNode;
            solvable = false;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }

        else {
            return solutionMoves;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            Node n = finalNode;
            Stack<Board> solution = new Stack<Board>();

            while (n != null) {
                solution.push(n.board);
                n = n.previous;
            }
            return solution;
        }

        else {
            return null;
        }
    }

    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of priority = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}