package Project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;


public class LocalSearchSA implements SukoduSolver{
	HashMap<Integer, Integer> sequence = new HashMap<>();
	Board board;
    Random rand = new Random(); 
    int noOfFE = 0;
    int noOfRep = 0;
    // new population at 1
    // cost calculation at 2
    //new state at 3
	
	LocalSearchSA(Board puzzle) {
		// TODO Auto-generated constructor stub
		board = puzzle;
	}
	
	public  void solveSudoku() {
		//initializing population in value 1
		boolean solutionFound = false;
		double decreaseFactor = 0.99;
		int noOfIterations = 100;
		double terminatingTemp = 0.01;
		double PAcceptWorse;
		double r;
		int newCost;
		int currCost;
		boolean solF = false;
		
		initializePopulation();
        System.out.println("Cost of Initial Population is: " + costFunction(1) );

		double startingT = startingTemp();
        //System.out.println("Starting Temp :" + startingT );
        //printBoard(1);
		//untill our termination condition of temp is not reached.
        while(startingT > terminatingTemp) {
        	
        	//no of iterations for each new temp
        	for(int i = 0; i < noOfIterations; i ++) {
        		//new state 
        		newState();
        		currCost = costFunction(1); 
        		newCost = costFunction(3); 
        		//if the cost of new state is less 0, means solution is found
        		if(newCost< 0) {
        			solutionFound = true;
        			System.out.println("Solution Found " );
        			solF = true;
        			break;
        		}
        		//if the cost of new state is less than the old state
        		//replace the new state
        		if( newCost < currCost) {
        			//System.out.println("New state is better than previous state");
        			noOfRep ++;
        			CopyBoard(3, 1);
        		}
        		else { 
        			//accepting worst solution if the random number generated is less than the 
        			//Temperature
        			PAcceptWorse = Math.exp(-(newCost - currCost) / startingT);
        			r = rand.nextFloat(0,1);
        			if(r < PAcceptWorse) {
        				noOfRep++;
        				CopyBoard(3, 1);
        			}        			
        		}
        	}
        	if(solF) {
        		break;
        	}else {
        	//after 100 iterations reduce the temp
        	startingT = startingT * decreaseFactor; 
        	//System.out.println("Starting Temp :" + startingT );
        	}
        }if(!solF) {
        System.out.println("Approximate Solution: " );
        }
        printBoard(3);
        System.out.println("Cost of Final Solution : "+ costFunction(3) );
        System.out.println("No of Function Evaluations: " + noOfFE);
        System.out.println("No of Function Replacements: " + noOfRep);
        
	}
		
	
	//finding the starting temp for the SA
	private double startingTemp() {
		
		int[] costArray = new int [10];
		double sum = 0;
		double k = (1.380649) * (10^(-23));

		//Finding the cost of 10 new states
		for(int i = 0; i <10; i ++) {
			newState();
			costArray[i] = costFunction(3);
			sum += costArray[i];
		}
		
	    double mean = sum / 10;
	    //return  (-mean / (k * (0.95)));
	    
	     //calculate the standard deviation for 10 new states
	    double standardDeviation = 0.0;
	    for (double num : costArray) {
	        standardDeviation += Math.pow(num - mean, 2);
	    }
	    //average of standard deviation of the cost of 10 new states 
	    //is the starting temp
	    return Math.sqrt(standardDeviation / 10);
	}
	
	private void printBoard(int value) {
		//check the population
		System.out.println("Printing Board:");
		for (int i = 0; i < board.row; i++) {
			for (int j = 0; j < board.column; j++) {
				System.out.print(board.boardArr[i][j][value] + "\t");            
			}
				System.out.print("\n");
		}
	}
	
	//initialize the new 2d array with random values within the grid. 
	//such that 1-9 values in the grid will not repeat
	private void initializePopulation() {
		
		CopyBoard(0,1);
        for (int i = 0; i < board.row; i++) {
            for (int j = 0; j < board.column; j++) {
            	
            	if(board.boardArr[i][j][1] == 0) {
            		//each call will fill the squares
            		fillMissingValues( i, j);
            	}
            }
        }        
	}
	
	private void CopyBoard(int a, int b) {
        for (int i = 0; i < board.row; i++) {
            for (int j = 0; j < board.column; j++) {
            	
            	board.boardArr[i][j][b] = board.boardArr[i][j][a];            
            }
        }
	}
	//in the sub square fill the missing values such that 1-9 values in the 
	//sub grid are not repeated. They might repeat in the row or column but
	//will be unique in the sub grid
	private void fillMissingValues(int x, int y) {
		int sPx = x/3 *3;
		int sPy = y/3 * 3;
		int value;
		initilizeSequence();
		
		
		//finding the missing values in sequence in square
		for(int i=0; i < 3; i++) {
			for(int j=0; j <3; j++ ) {
				
				if(board.boardArr[sPx + i][sPy + j][1] != 0 ) {
					sequence.put(board.boardArr[sPx + i][sPy + j][1] , 1);
				}
			}
		}
		
		ArrayList<Integer> arr = new ArrayList<>();
		for (Map.Entry<Integer, Integer> set :
			sequence.entrySet()) { 						
			if(set.getValue() == 0) {
				arr.add(set.getKey());
			}
		}
		
		//fill the missing values of sequence in squares
		for(int i=0; i < 3; i++) {
			for(int j=0; j <3; j++ ) {
				
				if(board.boardArr[sPx + i][sPy + j][1] == 0 ) {
					value = rand.nextInt(arr.size());
					board.boardArr[sPx + i][sPy + j][1] = arr.get(value);
					arr.remove(value);			
				}
			}
		}
	}
	
	//Cost Function calculates the cost of puzzle,
	//by counting the repeated values from rows and columns
	private int costFunction(int value) {
		noOfFE++;
		int cost = 0;
		int count = 0;
		
		//sequence of 1-9 numbers
		initilizeSequence();
		
		//no of repeated digits in rows
		for(int i=0; i < board.row; i++) {
			
			for(int j=0; j < board.column; j++ ) {
				
				//fill the 3rd array of 2d array with 0s
				board.boardArr[i][j][2] = 0;
				
				count = sequence.get(board.boardArr[i][j][value]);
				sequence.put(board.boardArr[i][j][value] , ++count);
				count = 0;
			}
			
			//put the row cost value in values of 2nd value of 3d array
			for (Map.Entry<Integer, Integer> set :
				sequence.entrySet()) {
				if(set.getValue() > 1) {
					count ++;
				}
			}
			board.boardArr[i][0][2] = count;
			count = 0;
			initilizeSequence();
		}
		
		//count no of repeated digits in columns
		for(int i=0; i < board.row; i++) {
			
			for(int j=0; j < board.column; j++ ) {
								
				count = sequence.get(board.boardArr[j][i][value]);
				sequence.put(board.boardArr[j][i][value] , ++count);
				count = 0;
			}
			
			//put the column cost value in values of 3d array
			for (Map.Entry<Integer, Integer> set :
				sequence.entrySet()) {
				if(set.getValue() > 1) {
					count ++;
				}
			}
			board.boardArr[i][1][2] = count;
			count = 0;
			initilizeSequence();
		}
		
		
		//counting the cost of row and column
		for(int r=0; r < board.row; r++) {
			cost = cost + board.boardArr[r][0][2] + board.boardArr[r][1][2];
		}
		
		
        //printing the cost 2D array of cost
		//System.out.println("Checking the cost");
        //for (int i = 0; i < board.row; i++) {
        //    for (int j = 0; j < board.column; j++) {
        //        System.out.print(board.boardArr[i][j][2] + "\t");            }
        //    System.out.print("\n");
        //}
		
		return cost;
		
	}
	//New state achieved by randomly selecting the grid
	//and then swaping the two unfixed values within that grid
	private void newState() {
	    int random_x = rand.nextInt(1,9);
	    int random_y = rand.nextInt(1,9);
	    
	    ArrayList<Integer> array = new ArrayList<Integer>();
	    
	    //randomly selecting the square
		int sPx = random_x/3 *3;
		int sPy = random_y/3 * 3;

		//finding all the unfixed values in the small square
		for(int i=0; i < 3; i++) {
			for(int j=0; j <3; j++ ) {
				if(board.boardArr[sPx + i][sPy + j][0] == 0) {
					array.add(sPx + i);
					array.add(sPy + j);
				}
			}
		}
		//counting the unfixed values
		
		int count = array.size() /2;
		if(count > 1) {
		random_x = rand.nextInt(1,count);
		
		random_y = (random_x + 1) % (count + 1);
		if(random_y == 0){
			random_y =1;
		}
		int x1 = array.get((random_x * 2)-2);
		int y1 = array.get((random_x * 2)-1);

		int x2 = array.get((random_y * 2)-2);
		int y2 = array.get((random_y * 2)-1);
		
		CopyBoard(1, 3);
		
		int temp = board.boardArr[x1][y1][3];
		board.boardArr[x1][y1][3] = board.boardArr[x2][y2][3];
		board.boardArr[x2][y2][3] = temp;
		}
		//if unfixed values are not more than 1 
		//else new state will be the same as old one		
	}
	
	private void initilizeSequence() {
		
		for(int s = 1; s <= 9; s ++)
		{
			sequence.put(s, 0);
		}
	} 

}
