# UQAI18
UQ AI project 2018

This is a motion-planning project performed at Univerisity of Queensland, Brisbane, in an introductory class COMP3702 on Artificial Intelligence by authors sigveks94 and ErlendHS.
Full task description: http://robotics.itee.uq.edu.au/~ai/lib/exe/fetch.php/wiki/assignment1-0901.pdf 



State space:

	R(x,y,a) - this is the state space of the robot
	Mb_i(x,y) - this is the state space of a moving box
	Mo_i(x,y) - this is the state space of a moving obstacle
	Si_i(x,y) - static obstacle


Actions:

R(x + dx, y + dy, a + da)

Transition:

	S x A. 

	So this is the result of the action on the specific state space, making a new state.

	R(x + dx, y + dy, a + da) X Mb_i(x + dx,y) or Mb_i(x, y + dy) X ...


-Certain contact needed to move boxes
-Cant move when collisions

Reward:

-distance between position and goal
-(Only reward for finish?? - maybe not optimal)

-mentioned something about the multi arm bandit problem -> somehow balance the weight of different heuristics.

Percept:

	Same as state space in essence. Fully observable case.

	Sample a lot around the known position of the obstacles.


Make a search taking you to a place where there are obstacles. Just use a path if you found any.



	Sample-strategy or discretization strategy?



TO DO: META-LEVEL:
1) Generate C-space from given input - already implemented in source code
2) Analyse C-space to map to fitting strategy (eg: calculate total area occupied by obstacles, number of boxes etc)
3) Sample-phase
	--> see 7.1.3 in additional litterature from MIT for different sampling strategies. 
4) Route-phase
	--> have to be careful about the 0.001 restrict in movement. Possible strategy (?) ; connect nodes in the 			roadmap created from the sample, and move the box along the edge connecting these nodes by 0.001? Can´t 			think 	of any quicker way to solve. Will be a lot of computing if we should sample nodes with 0.001 			density.
5) Moving-phase
6) Recurrence of 3,4,5 for each box
7) Validity-checking-phase



STRATEGY - A VARIANT OF THE PRM: 

SAMPLE: Sample nodes randomly across the board - discard nodes in collision - nodes must be sampled on either the same x-or y-value of neigboring nodes since robot can only move boxes perpendicularly. Higher density of nodes around the obstacles should be implemented (eg: one node around each corner). Remember to sample the nodes around each corner at such a distance that the box can move around the obstacle.

EDGES: Connect the k nearest nodes with euclidean distance less than d. If this cannot be done, add additional nodes to the sample around the area of the troubled node - repeat until all nodes are connected to at least k other nodes. Include checking of edges so that they only allow edges that are "wide enough" to allow worst case scenario of boxes and agent to pass through crevice. All edges used to move boxes must be perpendicular. Perhaps not needing to add edges between nodes that are already indirectly connected through other noeigboring nodes to speed up the building phase.

PATH: Connect start and goal node to a neighboring node and perform an a*-search to find a path. Use the number of robot steps as costs between edges. Use the straight line calculation of number of robot steps from a node to the goal node as heuristic function.

MOVE: Actually move box

REDO FOR ALL BOXES. KEEP NODES AND EDGES, BUT CHECK IF THEY ARE STILL VALID IN THIS NEW SITUATION/CONFIGURATION WEREAS A BOX HAS BEEN MOVED. IF SOME ARE NOT VALID - REDO EDGES-PHASE
