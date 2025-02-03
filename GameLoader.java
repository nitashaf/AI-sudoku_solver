package Project1;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class GameLoader {
    Board currBoard ;//Board obj
    Board storageBoard;
    String gameName;//File Name

    
    GameLoader(){
    	currBoard = new Board();
    	storageBoard = new Board();
    }
    
     
    public void copyBoard() {
        for (int i = 0; i < currBoard.row; i++) {
            for (int j = 0; j < currBoard.column; j++) {
            	storageBoard.boardArr[i][j][0]  =  currBoard.boardArr[i][j][0];            
            }
        }
    }
    
    public void copyBoard2() {
        for (int i = 0; i < currBoard.row; i++) {
            for (int j = 0; j < currBoard.column; j++) {
            	currBoard.boardArr[i][j][0] = storageBoard.boardArr[i][j][0];            
            }
        }
    }

    public void loadBoard(String gName){//populate 2D array of strings from a specified 9x9 CSV

        //Scanner uInp = new Scanner(System.in);//Grabs user input for fileName
        //System.out.println("Enter Puzzle Name: ");
        //gameName = uInp.nextLine();
    	gameName = gName;

        try {
            File file = new File("C:\\Users\\nitas\\Downloads\\Puzzles/" + gameName + ".csv");//Selects desired file from dir.
            Scanner read = new Scanner(file);//File reader
            while(read.hasNextLine()) {
                for (int i = 0; i < currBoard.row; i++) {
                    String[] line = read.nextLine().split(",");//Split each row into array
                    for (int j = 0; j < currBoard.column; j++) {
                        String q = "?";
                        String l = line[j];
                        //If a square from the puzzle is a '?' replace it with a zero on the board.
                        if (q.compareTo(l) == 0){
                            currBoard.boardArr[i][j][0] = 0;//Copy column value from array into board
                        }
                        else{
                            //The try catch block is to keep any 'skinwalker' question marks from sneaking through and
                            //crashing the program because apparently that's something that can happen.
                            try {
                                currBoard.boardArr[i][j][0] = Integer.parseInt(line[j]);//Parse the int for top layer
                            }
                            catch(Exception weird){
                                currBoard.boardArr[i][j][0] = 0;
                            }
                        }
                        //Filling in domain values, if it is a starting value, no possibilities are added.
                        for (int k = 1; k < currBoard.possValues; k++){
                            if(currBoard.boardArr[i][j][0] == 0){
                                currBoard.boardArr[i][j][k] = k;
                            }
                            else{
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {//Input file error catch
            e.printStackTrace();
        }
    }

}
