import edu.princeton.cs.algs4.Queue;

public class Board {
    private int[][] boardBlocks;
    private int boardDimension;

    // construct a board from an n-by-n array of blocks
    public Board(int[][] blocks){
        boardDimension = blocks.length;

        boardBlocks = new int[boardDimension][boardDimension];

        // (where blocks[i][j] = block in row i, column j)
        for(int i = 0; i < boardDimension; i++){
            for(int j = 0; j< boardDimension; j++){
                boardBlocks[i][j] = blocks[i][j];
            }
        }

    }

    // board dimension n
    public int dimension(){
        return boardDimension;
    }



    // number of blocks out of place
    public int hamming() {
        int hammingSum = 0;

        for(int i = 0; i < boardDimension; i++){
            for(int j = 0; j< boardDimension; j++){
                int blockGoalValue =  j+1+(i*boardDimension);
                int currentBlockValue = boardBlocks[i][j];
                //if value is not in ascending order then block is out of place, 0 doesn't count
                if(currentBlockValue != blockGoalValue && currentBlockValue != 0){
                    hammingSum++;
                }
            }
        }
        return hammingSum;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattanSum = 0;
        for(int i = 0; i < boardDimension; i++){
            for(int j = 0; j< boardDimension; j++){
                int blockGoalValue =  j+1+(i*boardDimension);
                int currentBlockValue = boardBlocks[i][j];
                //if value is not in ascending order, then block is out of place. 0 doesn't count
                if(currentBlockValue != blockGoalValue && currentBlockValue != 0){
                    //calculate distance from goal
                    int desiredRowNumber = (currentBlockValue-1) / boardDimension;
                    int desiredColumnNumber = (currentBlockValue-1) % boardDimension;

                    //count number of rows and columns from desired location
                    int difference = Math.abs(i-desiredRowNumber) + Math.abs(j-desiredColumnNumber);

                    manhattanSum += difference;
                }
            }
        }

        return manhattanSum;

    }

    // is this board the goal board?
    public boolean isGoal(){
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        Integer firstNonZeroRow = null;
        Integer firstNonZeroColumn = null;
        Integer secondNonZeroRow = null;
        Integer secondNonZeroColumn = null;
        for(int i = 0; i < boardDimension; i++){
            for(int j = 0; j< boardDimension; j++){
                int currentBlockValue = boardBlocks[i][j];

                //get first 2 non zero items for swapping
                if(currentBlockValue !=0 && firstNonZeroRow == null){
                    firstNonZeroRow = i;
                    firstNonZeroColumn = j;
                    continue;
                }
                if(currentBlockValue !=0 && firstNonZeroRow != null && secondNonZeroRow == null){
                    secondNonZeroRow = i;
                    secondNonZeroColumn = j;
                    //make sure this breaks out?????
                    break;
                }
            }
        }
        int firstNonZeroValue = boardBlocks[firstNonZeroRow][firstNonZeroColumn];
        int secondNonZeroValue = boardBlocks[secondNonZeroRow][secondNonZeroColumn];

        Board boardCopy = new Board(boardBlocks);

        //swap the block values
        boardCopy.boardBlocks[firstNonZeroRow][firstNonZeroColumn] = secondNonZeroValue;
        boardCopy.boardBlocks[secondNonZeroRow][secondNonZeroColumn] = firstNonZeroValue;

        return boardCopy;
    }

    public boolean equals(Object y){
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        //see if dimensions are different
        if(this.dimension() != that.dimension()){
            return false;
        }

        //see if all blocks are equal
        for(int i = 0; i < boardDimension; i++){
            for(int j = 0; j< boardDimension; j++) {
                if(this.boardBlocks[i][j] != that.boardBlocks[i][j]){
                    return false;
                }
            }
        }

        return true;
    }        // does this board equal y?

    // all neighboring boards
    public Iterable<Board> neighbors(){
        Block zeroBlock = null;
        Queue<Board> neighboringBoards = new Queue<>();
        for(int i = 0; i < boardDimension; i++){
            for(int j = 0; j< boardDimension; j++){
                int currentBlockValue = boardBlocks[i][j];

                //get first 2 non zero items for swapping
                if(currentBlockValue == 0 && zeroBlock == null){
                    zeroBlock = new Block(i,j,0);
                    break;
                }
            }
        }

        //get any valid neighboring swaps
        if(zeroBlock.row - 1 >= 0){
            //adjacentBlocks.enqueue(new Block(zeroBlock.row - 1, zeroBlock.column, boardBlocks[zeroBlock.row - 1][zeroBlock.column]));
            Board neighbor = new Board(boardBlocks);
            //swap top neighbor
            neighbor.boardBlocks[zeroBlock.row][zeroBlock.column] = neighbor.boardBlocks[zeroBlock.row-1][zeroBlock.column];
            neighbor.boardBlocks[zeroBlock.row-1][zeroBlock.column] = 0;
            neighboringBoards.enqueue(neighbor);
        }
        if(zeroBlock.row + 1 < boardDimension){
            Board neighbor = new Board(boardBlocks);
            //swap bottom neighbor
            neighbor.boardBlocks[zeroBlock.row][zeroBlock.column] = neighbor.boardBlocks[zeroBlock.row+1][zeroBlock.column];
            neighbor.boardBlocks[zeroBlock.row+1][zeroBlock.column] = 0;
            neighboringBoards.enqueue(neighbor);
        }
        if(zeroBlock.column - 1 >= 0){
            Board neighbor = new Board(boardBlocks);
            //swap left neighbor
            neighbor.boardBlocks[zeroBlock.row][zeroBlock.column] = neighbor.boardBlocks[zeroBlock.row][zeroBlock.column-1];
            neighbor.boardBlocks[zeroBlock.row][zeroBlock.column-1] = 0;
            neighboringBoards.enqueue(neighbor);
        }
        if(zeroBlock.column + 1 < boardDimension){
            Board neighbor = new Board(boardBlocks);
            //swap right neighbor
            neighbor.boardBlocks[zeroBlock.row][zeroBlock.column] = neighbor.boardBlocks[zeroBlock.row][zeroBlock.column+1];
            neighbor.boardBlocks[zeroBlock.row][zeroBlock.column+1] = 0;
            neighboringBoards.enqueue(neighbor);
        }

        return neighboringBoards;
    }

    private class Block{
        public int row;
        public int column;
        public int value;

        Block(int i, int j, int val){
            row = i;
            column = j;
            value = val;
        }

    }

    // string representation of this board
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(boardDimension + "\n");
        for (int i = 0; i < boardDimension; i++) {
            for (int j = 0; j < boardDimension; j++) {
                s.append(String.format("%2d ", boardBlocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args){

    } // unit tests (not graded)
}