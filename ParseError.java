/*
 * Author: Ethan Welsh
 * Teacher: George Novaky (CS1501)
 * Date: September 23rd, 2013
 * Assignment: 2
 */

class ParseError extends Exception
{
	private String s;
	private final boolean error;
	String errorString;

	public ParseError(String x) 
	{
		s = x;
		error = isError();
		if(isError()) errorString = whatError();
	}

	public String getErrorString() 
	{
		return errorString;
	}

	String whatError()
	{
		s = s.replaceAll(" ", "");

		for(char c : s.toCharArray()) if( (! ( (int) c >= (int)'A' && (int) c <= (int)'Z') ) && c != '!' && c != '^' && c != 'v' && c != '(' && c != ')') 
		{ // If you character isn't a letter between A and Z or a ^, v, !, (, or ) then we've got a problem...
			return (c + " is not a legal operator ");
		}


		char[] r = s.toCharArray();
		int rank = 0;

		for(char c : r) // Go through the entire array
		{
			switch(c) // And count to make sure that for every '(' there is a ')'
			{
			case '(':
				rank++;
				break;
			case ')':
				rank--;
				break;
			default:
			}
		}

		if(rank != 0)
		{
			if(rank > 0) return "Paranthetic Error: Missing a right paranthese!";
			if(rank < 0) return "Paranthetic Error: Missing a left parantheses!";
		}

		s = s.trim();

		for(int i = 0; i<r.length; i++) 
		{ // If there are illegal characters to the right or left of an atom...
			if((int)r[i] >= (int)'A' && (int) r[i] <= (int)'Z') 
			{
				if(r[i-1] != '!' && r[i-1] != '(' && r[i-1] != 'v' && r[i-1] != '^') {
					return ("Illegal Character: It is illegal for " + r[i] + " to have a '" + r[i-1] + "' to the left of it.");

				}

				if(r[i+1] != ')' && r[i+1] != '^' && r[i+1] != 'v') 
				{
					return ("Illegal Character: It is illegal for " + r[i] + " to have a '" + r[i+1] + "' to the right of it.");	
				}
			} 
			else if (r[i] == 'v' || r[i] == '^') 
			{
				if ((!(r[i+1] >= (int)'A' && (int) r[i+1] <= (int)'Z')) && r[i+1] != '(') 
				{
					return ("Illegal Character: A non-atom must begin with '('.");
				}
			}
		}
		return "Ruh-roh";
	}

	public boolean isError() 
	{
		s = s.replaceAll(" ", "");
		for(char c : s.toCharArray()) if( (! ( (int) c >= (int)'A' && (int) c <= (int)'Z') ) && c != '!' && c != '^' && c != 'v' && c != '(' && c != ')') {
			//System.out.println("Illegal Character '" + c + "'");
			s.replace(c, '?'); // If it isn't a letter
		}
		if(s.contains("?")) return true; // If our expression contains any unauthorized characters...

		char[] r = s.toCharArray();
		int rank = 0;

		for(char c : r) 
		{
			switch(c) 
			{
			case '(':
				rank++;
				break;
			case ')':
				rank--;
				break;
			default:
			}
		}

		if(rank != 0) return true; // If parentheses are messed up...

		s = s.trim();

		for(int i = 0; i<r.length; i++) 
		{ // If there are illegal characters to the right or left of an atom...
			if((int)r[i] >= (int)'A' && (int) r[i] <= (int)'Z') 
			{
				if(r[i-1] != '!' && r[i-1] != '(' && r[i-1] != 'v' && r[i-1] != '^') return true; // Check the char to the left of i.
				if(r[i+1] != ')' && r[i+1] != '^' && r[i+1] != 'v') return true; // and the right...

			} 
			else if (r[i] == 'v' || r[i] == '^') if ((!(r[i+1] >= (int)'A' && (int) r[i+1] <= (int)'Z')) && r[i+1] != '(') return true; // If this char is a !, and the next one isn't an atom or a '('
		}
		return false;
	}
}