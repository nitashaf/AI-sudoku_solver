# AI Project1
This project aimed to find optimal search methods to generate valid solutions for a Sudoku puzzle, a constraint satisfaction problem. The five algorithms implemented and tested were the following: 
Simple Backtracking (BT), Backtracking with Forward Checking (FC), Backtracking with Arc Consistency (AC), Local Search with Simulated Annealing and a Minimum Conflict
Heuristic (SA), and finally, Local Search using a Genetic Algorithm with a Penalty Function and Tournament Selection (GA). 
Algorithm efficiency was determined by measuring the number of evaluations being made, the success rate, and the number of cell replacements performed.
Testing determined, of the deterministic methods, AC had the best performance while finding a solution overall. The stochastic methods were unfortunately not able to consistently find a valid solution, but of the two, GA had better measured performance overall, while SA was closer to
finding a solution.

Problem Statement
Problem: Given a constraint satisfaction problem (CSP) there are many ways to approach and produce a valid solution. In the case of the Sudoku puzzle, the problem presented is to figure out which approach is best for puzzles of varying difficulty. These approaches differ in complexity, processing time, and overall effectiveness based upon how hard each problem is.
