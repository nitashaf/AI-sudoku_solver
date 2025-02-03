package Project1;

public class BackTracking implements SukoduSolver{
	Board board;
	int noofEF= 0;
	int noofRep = 0;

	BackTracking(Board puzzle){
		board = puzzle;
	}
	
	public  void solveSudoku() {
		
		//starting cell index		
		int x = 0;
		int y = -1;
		
		//if Simple back tracking returns true
		//it means solution is found
		if(simpleBacktracking(x, y)){
			
			//printing the solution
			System.out.println("No of Function Evaluations: " + noofEF);
			System.out.println("No of Function Replacements: " + noofRep);
			
			System.out.println("Solution Found: ");
			board.printBoard();
			
		}else {
			System.out.println("Solution Not Found: ");
		}
	}
	
	private boolean simpleBacktracking(int x, int y) {
		
		//System.out.println("In the solver: ");
		
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
		
			for(int pv=1;  pv<=9; pv++ ) {
		    //check if the value is valid
				
				if(isValid(pv, nextx, nexty)) {
					//System.out.println("Value placed"+ pv);
					
					//if value is valid place the value in the cell
					//move to the next cell using recursion
					board.boardArr[nextx][nexty][0] = pv;
									
					if(simpleBacktracking(nextx, nexty))
					{
						return true;
					}else {
						
					//coming back to the last stage and undo the placement 
					//System.out.println("Coming back");
					//System.out.println("Removing the last value" 
					//+ pv + " at x and y "+ nextx + " , "+ nexty);
						noofRep ++;
					board.boardArr[nextx][nexty][0] = 0;
					}
				}
			}
	    } //if we do not find empty cell, 
		else {
			//start the new cell
			if(simpleBacktracking(nextx, nexty)) {
				return true;
			}
		}
		return false;
	}
	
	
	//function to check the validity 
	//it will behave as cost function in these deterministic algorithms
	private boolean isValid(int Pvalue, int x, int y) {
		
		//checking the validity of sub square
		//System.out.println("Pvalue: "+Pvalue);
		noofEF ++;
		int sPx = x/3 *3;
		int sPy = y/3 * 3;

		for(int i=0; i < 3; i++) {
			for(int j=0; j <3; j++ ) {
				if(board.boardArr[sPx + i][sPy + j][0] == Pvalue) {
					return false;
				}
			}
		}
		
		//checking the validity of rows 
		for(int j = 0; j < board.row; j ++) {
			if(board.boardArr[x][j][0] == Pvalue) {
				return false;
			}
			//checking the validity of columns 
			if(board.boardArr[j][y][0] == Pvalue) {
				return false;
			}
		}
		return true;	
	}
}
