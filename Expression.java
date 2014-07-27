import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/*
 * Author: Ethan Welsh
 * Teacher: George Novaky (CS1501)
 * Date: September 23rd, 2013
 * Assignment: 2
 */

public class Expression 
{
	private String s = "";
	Node root;

	public Expression(String x) throws FileNotFoundException 
	{
		//creates a new Expression object and its tree and displays the tree.
		s=x;
		root = new Node(s);
		buildTree(root);

		Scanner s = new Scanner(new File("data2eval.txt"));

		while(s.hasNext()) 
		{
			String line = s.nextLine();
			if(line.charAt(0) != '(') {
				String[] lame = line.split(" ");
				setAtom(lame[0], lame[1]);
			}			
		}		
	}

	private void buildTree(Node m) 
	{ // Construct the tree from the expression
		String x = m.getData();
		int r = findMostSignificant(x); // Find the outermost operator of the expression (surrounded by the fewest number of parentheses).
		if(r == -1) 
		{ // If the expression has been split enough.
			for(char c : m.getData().toCharArray()) if(c == '(' || c == ')') m.setData(m.getData().replace(c, ' ')); // If it is a parentheses, kill it
			m.setData(m.getData().trim()); // Trim off the white space.
			return;
		}

		char splitChar = x.charAt(r); // Split on the most significant character (MSC).

		if(splitChar == 'v' || splitChar == '^') 
		{ // Set the lef and right nodes equal to the expression that is the left and right of the MSC
			String left = x.substring(0, r);
			String right = x.substring(r+1, x.length());
			m.setData(splitChar + "");
			m.setLeft(new Node(left));
			m.setRight(new Node(right));

			buildTree(m.getRight());
			buildTree(m.getLeft());
		} 
		else 
		{
			String right = x.substring(r+1, x.length());
			m.setData(splitChar + "");
			m.setRight(new Node(right));
			buildTree(m.getRight());
		}
	}

	private int findMostSignificant(String s) 
	{
		char[] x = s.toCharArray();
		int[] r = new int[s.length()];
		int rank = 0;
		int index = 0;

		for(char c : x) 
		{ // Increment through every character in your string.
			if(c == 'v' || c == '^' || c == '!') r[index] = rank;
			else 
			{
				switch(c) 
				{
				case '(': // Then the operators inside this '(' are deeper into your expression, so increment rank
					r[index] = 0;
					rank++;
					break;
				case ')':
					r[index] = 0;
					rank--;
				default:
					r[index] = 0;
				}
			}
			index++;
		}

		String rankString = "";
		for(int i : r) rankString = rankString + i;
		for(int i = 1; i < s.length(); i++) if(rankString.indexOf(gimmeDatChar(i)) != -1) return rankString.indexOf(gimmeDatChar(i));
		return -1;
	}

	private static char gimmeDatChar(int x)
	{
		return (char)(x + 48);
	}

	void setAtom(String atom, String value)
	{ // Set an atom equal to its corresponding boolean value.
		setAtomH(root, atom.charAt(0), Boolean.parseBoolean(value));
	}

	private static void setAtomH(Node n, char target, Boolean tf) 
	{ // Find the node, and set it equal to tf
		if(n.getData().charAt(0) == target) n.setBool(tf);
		if(n.getRight() != null) setAtomH(n.getRight(), target, tf);
		if(n.getLeft() != null) setAtomH(n.getLeft(), target, tf);
	}

	public boolean evaluate() 
	{ // Return the boolean evaluation of an expression
		return evaluateH(root);
	}

	private boolean evaluateH(Node n) 
	{ // Recursive helper function for evaluate()
		if(n.getLeft() == null && n.getRight() == null) return n.getBool(); // Is leaf
		else if (n.getData().equals("^")) // And
		{
            return evaluateH(n.getLeft()) && evaluateH(n.getRight());
		} 
		else if (n.getData().equals("v")) // or
		{
            return evaluateH(n.getLeft()) || evaluateH(n.getRight());
		}
		else if (n.getData().equals("!")) // not
		{
            return !evaluateH(n.getRight());
		}

		// evaluates this expression expression.
		return false;
	}

	public Expression copy() 
	{
		try {
			return new Expression(s); // Return a new expression build off of the old one's string.
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Verify directories and data files.");
			return null;
			
		}
	}

	Node normalize()
	{ // Normalize a tree
		while(isNormal(root) == false) root = normHelper(root);	
		return root;
	}

	Node normHelper(Node n)
	{ // recursive helper function for normalize
		switch(n.getData().charAt(0)) 
		{
		case '!':
			if(n.getRight().getData().charAt(0) == '!') // If you've got a ! over an !...
				return normHelper(n.getRight().getRight()); // Then cut these out of the tree.
			else if (n.getRight().getData().charAt(0) == 'v') // If you've got a ! over an ^
			{ 
				Node andNode = new Node("^"); // Create new nodes
				Node left = new Node("!");
				Node right = new Node("!");

				andNode.setLeft(left); // Set the two negations as the child of the ^ node
				andNode.setRight(right);

				left.setRight(normHelper(n.getRight().getLeft())); // And copy over the expression from then on.
				right.setRight(normHelper(n.getRight().getRight()));
				return andNode;
			}
			else return normHelper(n.getRight());
		case '^': 
			if(n.getRight().getData().charAt(0) == 'v') // If you've got an ^ over an v
			{ 
				Node orNode = new Node("v"); // Create new nodes
				Node left = new Node("^");
				Node right = new Node("^");

				orNode.setLeft(left); // Make v nodes child of the ^ node
				orNode.setRight(right);

				left.setLeft(n.getLeft()); // Set the left node of each of these children equal to the node that lay to the left of the and node
				right.setLeft(n.getLeft());

				left.setRight(normHelper(n.getRight().getLeft())); // And the right node should be whatever lay to the right and left of the previous v node.
				right.setRight(normHelper(n.getRight().getRight()));

				return orNode;
			} 
			else 
			{ // Keep going
				n.setLeft(normHelper(n.getLeft()));
				n.setRight(normHelper(n.getRight()));
				return n;
			}
		case 'v':
			n.setLeft(normHelper(n.getLeft()));
			n.setRight(normHelper(n.getRight()));
			return n;
		default:
			return n;
		}
	}

	Boolean isNormal(Node n)
	{ // Checks a tree to see if it is in normal form.
		if(n.getLeft() == null && n.getRight() == null) return true; // If you've reached an atom, return true.

		switch(n.getData().charAt(0)) 
		{ // If this node's symbol is a...
		case '!': 
			if(n.getRight().getData().charAt(0) == '!') return false; // And the next node is also a !, then this isn't normal
			else if (n.getRight().getData().charAt(0) == 'v') return false; // And the next node is a v, then this isn't normal
			else return isNormal(n.getRight()); // Otherwise, keep on checking
		case '^': 
			if(n.getRight().getData().charAt(0) == 'v') return false; // If you have a ^ over an v, then this expression isn't normal.
		default:
			return (isNormal(n.getRight()) && isNormal(n.getLeft())); // Keep checking...
		}
	}
	
	public void displayNormalized() 
	{ //displays the normalized tree.		
		this.normalize();
		TreeDisplay displayNorm = new TreeDisplay("Normalized " + this.toString());
		displayNorm.setRoot(this.root);	
	}
	
	public String toString() 
	{ // returns the print form of an expression.
		return s;
	}
}