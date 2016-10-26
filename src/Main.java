import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Main {

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        Board initial2 = new Board(blocks);

        Board thing = initial.twin();

        boolean isTrue = initial == initial;
        boolean isTrue2 = initial.equals(initial2);
        boolean isFalse = initial.equals(thing);

        Iterable<Board> neighbors = initial.neighbors();

        StdOut.println(initial);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
