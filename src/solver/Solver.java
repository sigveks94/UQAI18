package solver;

public class Solver {
	
	/*
	 * 
	 * Pseudocode: 
	 * 		
	 * Phase1 - Sampling phase
	 * 
	 * 	Sample initial node (deduced from intialRobotConfig) and goal nodes
	 * 	Sample nodes at corners of all obstacles, discard nodes in collision
	 * 	
	 * 	Collision detection:
	 * 		
	 * 		Simply check whether 2DPoint is occupied by box. A box spans a certain area in 2D.
	 *  
	 * 	
	 *  
	 * 	
	 * 	
	 * 
	 * Phase2 - Connecting phase 
	 * 	
	 * 	An edge is represented within a node with an edgeNodes list.
	 * 	Connect all nodes connected to the same box if collision free path.
	 * 	Create help nodes that serve to maintain left-right-up-down constraint, discard if in collision
	 * 	
	 * 
	 * 		
	 * 
	 * 	Collision detection:
	 * 
	 * 		Discretesize the line connecting two nodes by a density of w length.
	 * 		Check if collision with obstacle.
	 * 
	 * 
	 * 		
	 * 	 
	 * 
	 * Phase3 - Path phase
	 * 
	 * 	A* search with cost to current node and simple distance to goal heuristic.
	 * 		If no solution:
	 * 			
	 * 			Try to move the other box first. 
	 * 			
	 * 
	 * 			If still no solution; move obstacle.
	 * 
	 * 		
	 * Phase4 - Move phase 
	 * 
	 * 	remember new samples of nodes connected to moved box
	 * 	
	 * 	
	 * 
	 * Phase5 - Solution phase
	 * 	
	 * 	
	 *
	 * 
	 * 
	 * 
	 */
	

}
