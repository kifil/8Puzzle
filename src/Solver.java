import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

/**
 * Created by User on 10/25/2016.
 */
public class Solver {

    private static class SearchNode{
        public SearchNode previousNode;
        public Board board;
        public int movesMade;
        SearchNode(){
        }

        SearchNode(SearchNode previous, Board b, int m){
            previousNode = previous;
            board = b;
            movesMade = m;
        }

        private static final Comparator<SearchNode> ByManhattan  = new ByManhattan();

        private static class ByManhattan implements Comparator<SearchNode>{
            public int compare(SearchNode v, SearchNode w){
                //is this the right order?
                if(v.board.manhattan() + v.movesMade < w.board.manhattan() + w.movesMade){
                    return -1;
                }
                else if(v.board.manhattan() + v.movesMade > w.board.manhattan() + w.movesMade){
                    return 1;
                }
                else{
                    return 0;
                }
            }

        }
    }

    private Iterable<Board> solutionPath;
    private int minimumMovesToSolve;



    public Solver(Board initial){
        //use a minimum priority queue to keep track of moves made and which board to try to solve next
        MinPQ<SearchNode> boardPQ = new MinPQ<SearchNode>(SearchNode.ByManhattan);

        //insert initial board into priority queue
        boardPQ.insert(new SearchNode(null,initial,0));
        //remove node with lowest manhattan value
        SearchNode currentNode = boardPQ.delMin();

        //create a twin and
        //while(!currentNode.board.isGoal() && !twinNode.board.isGoal()){

        //while the next node is not the solution node
        while(!currentNode.board.isGoal()){
            Iterable<Board> neighbors = currentNode.board.neighbors();
            for (Board neighbor: neighbors) {
                //add neighbors to queue, but exclude the board identical to the current node
                if(neighbor != currentNode.board){
                    SearchNode nextNode = new SearchNode(currentNode,neighbor,currentNode.movesMade + 1);
                    boardPQ.insert(nextNode);
                }
            }
            //get the next highest priority node
            currentNode = boardPQ.delMin();
        }

        //currentNode is the goal
        SearchNode solutionNode = currentNode;
        minimumMovesToSolve = solutionNode.movesMade;

        Stack<Board> solutionBoardPath = new Stack<>();

        //create solution path by following previous nodes
        while(solutionNode.previousNode != null){
            solutionBoardPath.push(currentNode.board);
            solutionNode = solutionNode.previousNode;
        }

        //push initial node
        solutionBoardPath.push(solutionNode.board);

        solutionPath = solutionBoardPath;
    }

    // find a solution to the initial board (using the A* algorithm)
    public boolean isSolvable(){
        //TODO
        return true;
    }            // is the initial board solvable?

    public int moves(){
        return minimumMovesToSolve;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution(){
        return solutionPath;
    }      // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args){

    } // solve a slider puzzle (given below)
}