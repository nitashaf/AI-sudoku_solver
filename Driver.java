package Project1;

public class Driver {
    public static void main(String[] args) {
    	
        String[] allFiles = {"Easy-P1", "Easy-P2", "Easy-P3", "Easy-P4", "Easy-P5",
        		"Med-P1", "Med-P2", "Med-P3", "Med-P4", "Med-P5",
        		"Hard-P1", "Hard-P2", "Hard-P3", "Hard-P4", "Hard-P5",
        		"Evil-P1", "Evil-P2", "Evil-P3", "Evil-P4", "Evil-P5"};
        GameLoader game = new GameLoader(); 
        
        //System.out.println(game.currBoard.isInPossibilities(0,0, 4));
        //game.currBoard.removeFromPossibilities(0,0, 4);
        //LocalSearchGA ga = new LocalSearchGA(game.currBoard);
        //LocalSearchSA sa = new LocalSearchSA(game.currBoard);
        //BacktrackingAC btac = new BacktrackingAC(game.currBoard);   
        
        for(int i = 0; i < allFiles.length; i ++) {
        System.out.println("\n Problem: "+ allFiles[i]);
        	
        game.loadBoard(allFiles[i]);                
        game.copyBoard();
        System.out.println("Problem board: ");
        game.currBoard.printBoard();
               
        System.out.println("\n Solving using Backtracking: ");
        SukoduSolver ss = new BackTracking(game.currBoard);
        ss.solveSudoku();
 
        game.copyBoard2();
        System.out.println("\n Solving using Backtracking and Forward Checking: ");
        ss = new BacktrackingFC(game.currBoard);
        ss.solveSudoku();
        
        
        game.copyBoard2();
        System.out.println("\n Solving using Backtracking Arc Consistency: ");
        ss = new BacktrackingAC(game.currBoard);
        ss.solveSudoku();
        
        for(int p = 0; p < 10; p ++) {
        
        	game.copyBoard2();
        	System.out.println("\n Solving using Local Search GA: ");
        	ss = new LocalSearchGA(game.currBoard);
        	ss.solveSudoku();
        
        	
        	game.copyBoard2();
        	System.out.println(" \n Solving using Local Search SA: ");
        	ss = new LocalSearchSA(game.currBoard);
        	ss.solveSudoku();
        	}
       }
    }
    
}

