/*
 * Author: Ethan Welsh
 * Teacher: George Novaky (CS1501)
 * Date: September 23rd, 2013
 * Assignment: 2
 */

public class Node 
{

	Node left, right;
	String symbol;
	private Boolean tf; // Boolean value for this node

	public Node(String s) 
	{
		left = null;
		right = null;
		symbol = s;
	}
	
	public Node(String s, Node r, Node l) 
	{
		left = l;
		right = r;
		symbol = s;
	}

	public void setBool(Boolean pop) 
	{
		tf = pop;
	}
	
	public Boolean getBool() 
	{
		return tf;
	}
	
	public void setRight(Node r) 
	{
		right = r;
	}
	
	public Node getRight() 
	{
		return right;
	}
	
	public void setLeft(Node l) 
	{
		left = l;
	}
	
	public Node getLeft() 
	{
		return left;
	}
	
	public String getData() 
	{
		return symbol;
	}
	
	public void setData(String s) 
	{
		symbol = s;
	}

}