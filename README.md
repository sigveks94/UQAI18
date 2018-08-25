# UQAI18
UQ AI project 2018

TestTest

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

NB: Don´t need to worry about collision-checking.


TO DO: META-LEVEL:
1) Generate C-space from given input - already implemented in source code
2) Analyse C-space to map to fitting strategy (eg: calculate total area occupied by obstacles, number of boxes etc)
3) Sample-phase
4) Route-phase
5) Moving-phase
6) Recurrence of 3,4,5 for each box
7) Validity-checking-phase



