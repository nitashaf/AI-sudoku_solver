package Project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LocalSearchGA implements SukoduSolver{
	HashMap<Integer, Integer> sequence ;
	Board board;
    Random rand ;
    int tournamentSize = 5;
    int populationSize = 200;
    int testingSize = 150;
    int parentSize = 2;
    int noOfFE = 0;
    // new population at 1
    // cost calculation at 2
    // initializing the board of population from index 3
	
	LocalSearchGA(Board puzzle) {
		// TODO Auto-generated constructor stub
		board = new Board(populationSize);

		//initializing the total population size, but we will use testing Size
		for(int i = 0; i < board.row; i ++) {
			for(int j = 0; j < board.column; j ++) {
				board.boardArr[i][j][0] = puzzle.boardArr[i][j][0]; 
			}
		} 
		sequence = new HashMap<>();
	    rand = new Random(); 
	}
	
	public  void solveSudoku() {
		
		int[]parents = new int[parentSize];
		int approxSolutionIndex = 0;
		int approxSolutionCost = Integer.MAX_VALUE;
		int ParentCost;
		int ParentCMCost;
		boolean solF = false;
		int initialCostSum = 0;
		int initialCostAvg = 0;
		
		//initialize the population with random numbers
		//Population is initialized by placing random numbers in the 		
		for(int p = 3; p < testingSize + 4; p ++) {
			initializePopulation(p);
		}
		
		//avergaeCost of Initial population is 
		for(int c = 3; c < testingSize + 4; c ++) {
			initialCostSum += costFunction(c);
		}
		initialCostAvg = initialCostSum / testingSize;
		System.out.println("Cost of Initial Population is: " + initialCostAvg );
		
		
		for(int i = 0; i < 3000; i ++) {
			
			//first select 2 parents using tournament selection
			parents = tournamentSelection();
			//System.out.println("Parents Selectd are: Parent1 "+ parents[0]);
			//System.out.println("Parents Selectd are: Parent 2 "+ parents[1]);
			
			//cross over the parents
			crossover(parents);
			
			//mutate parents
			mutate(parents);

			//if new child children / parents have less cost,
			//then we will replace them back to population else not
			for(int k = 0; k < parentSize; k ++) {
				ParentCost = costFunction(parents[k]);
				ParentCMCost = costFunction(populationSize -(k+1));
				
				if(ParentCost < approxSolutionCost){
					approxSolutionCost = ParentCost;
					approxSolutionIndex = parents[k];
				}
				//if either original parent or new parent has 0 cost
				//means solution is found
				if(ParentCost == 0 ) {
					System.out.println("Solution Found :" );
					printBoard(parents[k]);
					approxSolutionIndex = parents[k];
					approxSolutionCost = ParentCost;
					solF = true;
					break; 				
				}
				//if either original parent or new parent has 0 cost
				//means solution is found
				if(ParentCMCost == 0) {
					System.out.println("Solution Found :" );
					printBoard(populationSize -(k+1));
					approxSolutionIndex = populationSize -(k+1);
					approxSolutionCost = ParentCMCost;
					solF = true;
					break;
				}else {
					//else if new parent after crossover and mutation is better
					//then replace it back in population
					if(ParentCMCost < ParentCost){
						//adding the replacements as well
						noOfFE++;
						approxSolutionIndex = parents[k];
						approxSolutionCost = ParentCMCost;
						CopyBoard(populationSize -(k+1), parents[k]);
					}
				}
			}
			if(solF) {
		        break;
			}
		}
		if(solF) {
	        System.out.println("Cost of Solution : "+ approxSolutionCost);
	        System.out.println("No of Function Evaluations: " + noOfFE);
		}else {
			System.out.println("Approximate Solution is ");
			printBoard(approxSolutionIndex);
			System.out.println("Cost of Approximate Solution : "+ approxSolutionCost);
			System.out.println("No of Function Evaluations: " + noOfFE);
		}
	}
		
	//New state achieved by randomly selecting the grid
	//and then swaping the two unfixed values within that grid
	private void mutate(int[] selectedParents) {
		//System.out.println("Parents before mutation: ");
		//System.out.println("Parents 1: ");
		//printBoard(selectedParents[0]);
		//System.out.println("Parents 2: ");
		//printBoard(selectedParents[1]);

		for(int i = 0; i < parentSize; i ++) {
			CopyBoard(selectedParents[i], populationSize -(i+1));
			newState(populationSize -(i+1));
			//if(costFunction(populationSize -(i+1)) > costFunction(selectedParents[i])){
			//	CopyBoard(selectedParents[i], populationSize -(i+1));
			//}
		}
		//System.out.println("Parents after mutation: ");
		//System.out.println("Parents 1: ");
		//printBoard(selectedParents[0]);
		//System.out.println("Parents 2: ");
		//printBoard(selectedParents[1]);
	}
	
	//swaping the first 3 columns of the two selected parents 
	private void crossover(int[] selectedParents) {
		
		//crossover of first three columns of board
		int [][]tempBoard = new int[9][9];
		
		for(int i = 0; i <9; i ++) {
			for(int j = 0; j < 3; j ++) {
				tempBoard[i][j]= board.boardArr[i][j][populationSize -1];
				board.boardArr[i][j][populationSize -1] = board.boardArr[i][j][populationSize -2];
				board.boardArr[i][j][populationSize -2] = tempBoard[i][j];
			}
		}
	}
	
	//select 5 random states from population
	//and run tournament in between them for cost
	private int[] tournamentSelection() {
		
		int[] array = new int [tournamentSize * 2];
		int [] parentIndex = new int [parentSize];
		int cost = Integer.MAX_VALUE;
		int index = 0;
		ArrayList<Integer> list = (ArrayList<Integer>) IntStream.range(3,testingSize + 5).boxed().collect(Collectors.toList());
		
		//parent size is 2, for every parent we will run the tournament selection
		for(int k = 0; k < parentSize; k ++) {
			
			//randomly selecting 5 tournament size population
			for(int i = 0; i < tournamentSize; i ++) {
				//using parentIndex as temp variable here
				parentIndex[k] = rand.nextInt(0,list.size()-1);
				array[i] = list.get(parentIndex[k]);
				list.remove(parentIndex[k]);
				//System.out.println("Cost of the parents," + costFunction(array[i]));
				array[i+ tournamentSize] = costFunction(array[i]);
				//find the one with min cost
				if(array[i+ tournamentSize] < cost) {
					cost = array[i+ tournamentSize];
					index = i;
				}
			}
			//System.out.println("Array of tournament 1 : ");
			//for(int l = 0; l < tournamentSize *2; l++) {
			//	System.out.print(array[l] + "\t" );
			//}
			parentIndex[k] = array[index];
			cost = Integer.MAX_VALUE;

		}
		return parentIndex;
	}
	
	
	private double startingTemp() {
		
		int[] costArray = new int [10];
		double sum = 0;
		double k = (1.380649) * (10^(-23));

		for(int i = 0; i <10; i ++) {
			newState(i);
			costArray[i] = costFunction(3);
			sum += costArray[i];
		}
		
	    double mean = sum / 10;
	    //return  (-mean / (k * (0.95)));
	    
	     //calculate the standard deviation
	    double standardDeviation = 0.0;
	    for (double num : costArray) {
	        standardDeviation += Math.pow(num - mean, 2);
	    }

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
	private void initializePopulation(int value) {
		
		CopyBoard(0,value);
        for (int i = 0; i < board.row; i++) {
            for (int j = 0; j < board.column; j++) {
            	
            	if(board.boardArr[i][j][value] == 0) {
            		//each call will fill the squares
            		fillMissingValues( i, j, value);
            	}
            }
        }        
	}
	//copy board to another 3d array board
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
	private void fillMissingValues(int x, int y, int key) {
		int sPx = x/3 *3;
		int sPy = y/3 * 3;
		int position;
		initilizeSequence();
		
		//finding the missing values in sequence in square
		for(int i=0; i < 3; i++) {
			for(int j=0; j <3; j++ ) {
				
				if(board.boardArr[sPx + i][sPy + j][key] != 0 ) {
					sequence.put(board.boardArr[sPx + i][sPy + j][key] , 1);
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
				
				if(board.boardArr[sPx + i][sPy + j][key] == 0 ) {
					position = rand.nextInt(arr.size());
					board.boardArr[sPx + i][sPy + j][key] = arr.get(position);
					arr.remove(position);			
				}
			}
		}
	}
	
	//Cost Function
	private int costFunction(int value) {
		
		noOfFE++;
		int cost = 0;
		int count = 0;
		
		initilizeSequence();
		
		//no of repeated digits in rows
		for(int i=0; i < board.row; i++) {
			
			for(int j=0; j < board.column; j++ ) {
				
				//fill the 3rd 2d array with 0s
				board.boardArr[i][j][2] = 0;
				
				count = sequence.get(board.boardArr[i][j][value]);
				sequence.put(board.boardArr[i][j][value] , ++count);
				count = 0;
			}
			
			//put the row cost value in values of 3d array
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
		
		//no of repeated digits in columns
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
	
	private void newState(int value) {
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

		
			int temp = board.boardArr[x1][y1][value];
			board.boardArr[x1][y1][value] = board.boardArr[x2][y2][value];
			board.boardArr[x2][y2][value] = temp;
		}
		//else new state will be the same as old one
	}
	
	private void initilizeSequence() {
		
		for(int s = 1; s <= 9; s ++)
		{
			sequence.put(s, 0);
		}
	} 

}



