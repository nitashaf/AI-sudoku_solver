package Project1;

public class BacktrackingAC implements SukoduSolver{
	Board board;
	int noofEF = 0;
	int noofRep = 0;

	BacktrackingAC(Board puzzle){
		board = puzzle;
	}
	
	public  void solveSudoku() {
		//starting cell index	
		int x = 0;
		int y = -1;
		
		//initializing arrays of Domain
		initializeArrays();
		
		//if back tracking returns true
		//it means solution is found
		if(BacktrackingACSudoku(x, y)){
			
			System.out.println("No of Function Evaluations: " + noofEF);
			System.out.println("No of Function Replacements: " + noofRep);
			System.out.println("Solution Found: ");
			board.printBoard();
		}else {
			System.out.println("Solution Not Found: ");
		}
	}
	
	private void initializeArrays() {
		//value 0 has original arrays
		//value 1, 2, 3 has array for rows, columns and grids
		//Domain is divided into three arrays,
	
		for(int k = 1; k < 4; k++ ) {
			for(int i = 0; i < board.row; i ++) {
				for(int j = 0; j < board.column; j ++) {
					board.boardArr[i][j][k] = 0;
				}
			}
		}
		
		//place 1 for digits that are fixed
		for(int x=0; x <board.row; x++) {
			for(int y= 0; y < board.column; y++) {
				if(board.boardArr[x][y][0] != 0) {
					board.boardArr[x][board.boardArr[x][y][0]-1][1] = 1;
					board.boardArr[y][board.boardArr[x][y][0]-1][2] = 1;
					board.boardArr[findGrid(x, y)][board.boardArr[x][y][0]-1][3] = 1;
				}
			}
		}
	}
	
	
	
	private boolean BacktrackingACSudoku(int x, int y) {
		
		//System.out.println("In the solver: ");
		int possibleValue;
		//base condition
		if(x == board.row-1 && y  ==  board.column -1) {
			return true;
		}		
		int nextx = x;
		int nexty = y;

		//when we reach the end of row, then move to next row
		if( nexty == board.column-1) {
			nextx = x+1;
			nexty = 0;
		}
		else {
			nexty = nexty + 1;
			}
		
		//System.out.println("x: "+nextx);
		//System.out.println("y: "+nexty);
		
		//when we found an empty cell
		if(board.boardArr[nextx][nexty][0] == 0) {
		
			for(int j= 0; j < board.column; j ++ ) {
				possibleValue = j + 1;
				
				//check if the value is in domains
				if(board.boardArr[nextx][j][1]== 0 
						&& board.boardArr[nexty][j][2] == 0
						&& board.boardArr[findGrid(nextx, nexty)][j][3] == 0) {
					
					//System.out.println("Possible expected Value: "+ possibleValue);
					
					//check if the value is valid
					if(isValid(possibleValue, nextx, nexty)) {
						//System.out.println("Value placed"+ possibleValue);
						
						//if value is valid place the value in the cell
						//move to the next cell using recursion
						//also mark them fixed in the domain
						board.boardArr[nextx][nexty][0] = possibleValue;
						board.boardArr[nextx][j][1] = 1;
						board.boardArr[nexty][j][2] = 1;
						board.boardArr[findGrid(nextx, nexty)][j][3] = 1;
					
						if(BacktrackingACSudoku(nextx, nexty)) {
							return true;
						}else {
							
							//coming back to the last stage and undo the placement 
							//undo from domain as well
							//System.out.println("Coming back");
							//System.out.println("Removing the last value" 
							//		+ possibleValue + " at x and y "+ nextx + " , "+ nexty);
							noofRep ++;
							board.boardArr[nextx][nexty][0] = 0;
							board.boardArr[nextx][j][1] = 0;
							board.boardArr[nexty][j][2] = 0;
							board.boardArr[findGrid(nextx, nexty)][j][3] = 0;
						}	
					}
					
				}
			} 
	    }
		else {
			//start the new cell
			if(BacktrackingACSudoku(nextx, nexty)) {
				return true;
			}
		}
		return false;
	}
	
	//finding the grid number for managing the domains
	private int findGrid(int x, int y) {
		int sPx;
		int sPy;
		int gridx;
		int gridy;
		int gridRow;
		
		sPx = (x/3 *3);
		sPy = (y/3 * 3);
		if(sPy == 0) {
			gridx = sPx + 1;
			gridy = sPy;
		}else {
			gridx = sPx + 1;
			gridy = Math.abs(sPy -4);
		}
		
		gridRow =  (gridx + gridy)-1;
		return gridRow;
	}
	
	//function to check the validity 
	//it will behave as cost function in these deterministic algorithms
	private boolean isValid(int Pvalue, int x, int y) {
		
		//System.out.println("Pvalue: "+Pvalue);
		noofEF ++;
		int sPx = x/3 *3;
		int sPy = y/3 * 3;

		//checking the validity of sub square
		for(int i=0; i < 3; i++) {
			for(int j=0; j <3; j++ ) {
				if(board.boardArr[sPx + i][sPy + j][0] == Pvalue) {
					//System.out.println("Already in the grid : " + board.boardArr[sPx + i][sPy + j][0]);
					return false;
				}
			}
		}
		//checking the validity of rows and columns
		for(int j = 0; j < board.row; j ++) {
			if(board.boardArr[x][j][0] == Pvalue) {
				//System.out.println("Already in the row");
				return false;
			}
			if(board.boardArr[j][y][0] == Pvalue) {
				//System.out.println("Already in the col");
				return false;
			}
		}
		return true;	
	}
	
	private void printBoard(int value) {
		
		System.out.println("Printing Board : ");
		System.out.print("\n");
        for (int i = 0; i < board.row; i++) {
            for (int j = 0; j < board.column; j++) {
                System.out.print(board.boardArr[i][j][value] + "\t");            
            }
            	System.out.print("\n");
        }
	}
}
