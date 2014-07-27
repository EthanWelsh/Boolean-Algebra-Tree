import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: Ethan Welsh
 * Teacher: George Novaky (CS1501)
 * Date: September 23rd, 2013
 * Assignment: 2
 */

public class ExpressionDisplayer 
{
	public static void main(String[] args) throws FileNotFoundException
	{
        System.out.println("Displaying");
		Scanner scan = new Scanner(new File("data.txt"));

		ArrayList<String> myExp = new ArrayList<String>();

		while(scan.hasNext()) myExp.add(scan.nextLine()); // Read the lines of this file into an arraylist.
			
		int num = 1;
		Scanner read = new Scanner(System.in);
		
		for(String y : myExp) 
		{ // For every string in the arraylist
			System.out.println("Continue? Y or N"); // Do you want to display this?
			if(read.next().equalsIgnoreCase("n")) 
			{
				System.exit(0);
			}
			
			ParseError p = new ParseError(y); // Make sure you aren't parsing the string wrong...
			if(p.isError()) // If you are...
			{
				System.out.println("\t\tError: \n" + "\t\t" + y + " " + p.errorString);
			} 
			else 
			{	
				Expression x = new Expression(y);
				System.out.println(num + ".\t" + y);
				
				TreeDisplay display = new TreeDisplay(x.toString()); // Display unnormalized.
				display.setRoot(x.root);
				
				x.displayNormalized();
				num++;
			}

		}
	}
}


