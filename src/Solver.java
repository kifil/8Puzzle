import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

/**
 * Created by User on 10/25/2016.
 */
public class Solver {
    private Iterable<Board> solutionPath;
    private int minimumMovesToSolve;
    private boolean isSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        //use a minimum priority queue to keep track of moves made and which board to try to solve next
        MinPQ<SearchNode> givenBoardPQ = new MinPQ<SearchNode>(SearchNode.ByManhattan);
        MinPQ<SearchNode> twinBoardPQ = new MinPQ<SearchNode>(SearchNode.ByManhattan);

        //start searching through given board and it's twin (to see if it is unsolvable)
        SearchNode currentNode = new SearchNode(null,initial,0);
        SearchNode currentTwinNode = new SearchNode(null,initial.twin(),0);

        //solve both given board and twin board in lock-step to see which one gets us a solution
        while(!currentNode.board.isGoal() && !currentTwinNode.board.isGoal()){

            //update priority queue for given board
            Iterable<Board> neighbors = currentNode.board.neighbors();
            for (Board neighbor: neighbors) {
                //add neighbors to queue, but exclude the board identical to the current node
                if(neighbor != currentNode.board){
                    SearchNode nextNode = new SearchNode(currentNode,neighbor,currentNode.movesMade + 1);
                    givenBoardPQ.insert(nextNode);
                }
            }
            //get the next highest priority node
            currentNode = givenBoardPQ.delMin();


            //update priority queue for twin board
            Iterable<Board> twinNeighbors = currentTwinNode.board.neighbors();
            for (Board twinNeighbor: twinNeighbors) {
                //add neighbors to queue, but exclude the board identical to the current node
                if(twinNeighbor != currentTwinNode.board){
                    SearchNode nextTwinNode = new SearchNode(currentTwinNode,twinNeighbor,currentTwinNode.movesMade + 1);
                    twinBoardPQ.insert(nextTwinNode);
                }
            }
            //get the next highest priority node
            currentTwinNode = twinBoardPQ.delMin();
        }

        //solution is unsolvable if only its twin can be solved
        if(currentTwinNode.board.isGoal()){
            isSolvable = false;
            minimumMovesToSolve = -1;
        }
        else{
            isSolvable = true;
            //currentNode is the goal
            SearchNode solutionNode = currentNode;
            minimumMovesToSolve = solutionNode.movesMade;

            Stack<Board> solutionBoardPath = new Stack<>();

            //create solution path by following previous nodes
            while(solutionNode.previousNode != null){
                solutionBoardPath.push(solutionNode.board);
                solutionNode = solutionNode.previousNode;
            }

            //push initial node
            solutionBoardPath.push(solutionNode.board);

            solutionPath = solutionBoardPath;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable(){
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        return minimumMovesToSolve;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        return solutionPath;
    }

    public static void main(String[] args){

    }

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
}