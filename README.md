Boolean-Algebra-Tree
====================
Boolean Expressions

Boolean expressions are complex boolean statements composed of atoms, unary, and binary 
operators. Atoms are assigned a boolean value (true or false), and can be evaluated into 
a boolean expression. For example, consider the following boolean expression regarding 
ABC, considering:

A = True
B = False
C = True

^ = and
v = or
! = not
(A ^ (B v C))

The above statement can be colloquially expressed as ‘A and B or C’ or ‘A and B or A and 
C’ and evaluates into true. The above statement represents a relatively simple boolean 
expression, however more complex boolean statements such as: 
‘(((P v Q) ^ (R v S)) ^ ((T v U) ^ ((V v W) ^ (X v Y))))’ necessitate an algorithmic 
approach to tackling these more complex problems. Perhaps the most simple way about going 
about solving this problem. Perhaps the most simple means of representing these boolean 
statements is through the use of trees. For example, the above expression could be 
visually expressed by the following tree:

									           ^
							  		         /	 \
			        	 					/	  \
			      					       A	   v
										         /    \	
									            B      C

Building these trees from the expression is also quite convenient. After isolating the 
outermost operator of the expression (the operator which is enclosed with the fewest 
amount of parentheses), the expression can be split on said operator into two different 
branches. These two different expressions can be further split into different branches 
until each Node on the tree contains only a single atom or operator. Evaluating the 
expression after the tree has been created is as simple at evaluating each node from the 
bottom up. Nodes which contain letters (atoms) are simple, as they are already assigned a 
boolean value. For operator nodes, one must look to see what the boolean values of the 
nodes to the left and right of the operator node is. For v (or) nodes, either the left or 
right children of said node must be true, otherwise the node will evaluate to false. The 
same is true with ^ (and) nodes, except that both children must evaluate to positive. 
Lastly the child of a ! (not) Node must be false in order for said node to evaluate to 
true. A preferable output for the above tree would be:
                                             
                                              v  
							  		       /     \
			        	 				  /	      \
			      					      ^	       ^
										/   \    /    \	
									   A     B  A      C
The trees evaluate into the same basic expression, but putting a tree into this form,
called disjunctive normal form where all terms in the algebraic expression have been 
distributed, and the expression is in its most simple form. Because of this, statements
which could have previously looked different such as (!(!((A ^ (B v C)))) would be 
revealed to actually have the same output.

In order to convert to this form, we use DeMorgan's law to distribute leading operators
across the expression such that:

   						   (A ^ (B v C))  --->  ((A ^ B) v (A ^ C)) 
                           ((A v B) ^ C)  --->  ((A ^ C) v (B ^ C)) 
                           
After converting an expression into its corresponding tree form, this algorithm is 
relatively simple to complete. The algorithm recursively traverses down a tree, and 
performs bottom up recursion searching for any conjunctions over disjunctions ('v' nodes 
that are parented by an '^' node). Once this has been found, a new node 'v' node is 
created, with two new '^' children which point to the nodes to the left and right of the
'v' node, and the node to the left of the '^' node. After the entire tree has been 
recursively checked for conjunctions over disjunctions, a new recursive call is made, 
searching for any double negations. After eliminating these, the tree is now in 
disjunctive normal form.
