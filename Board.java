package Project1;

public class Board {
//  Create an array of tuples that denote immutable pairs or just iterate through skipping anything that isn't a '?'
    int row ;
    int column ;
    int possValues ;
    int[][][] boardArr ;
    
    Board(){
        row = 9;
        column = 9;
        possValues = 10;
        boardArr = new int[row][column][possValues];
    }
    
    Board(int value){
        row = 9;
        column = 9;
        possValues = value;
        boardArr = new int[row][column][possValues];
    }



    public void printBoard(){//Iterate through board to print in a readable format
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.print(boardArr[i][j][0] + "\t");            }
            System.out.print("\n");
        }
    }
    



}

