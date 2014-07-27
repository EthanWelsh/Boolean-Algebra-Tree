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

public class ExpressionEvaluator
{
	public static void main(String[] args) throws FileNotFoundException
	{
		System.out.println("Evaluating");



        Scanner scan = new Scanner(new File("data2eval.txt"));

		ArrayList<String> myExp = new ArrayList<String>();

		String atoms = "";
		int i = 0;

		while(scan.hasNext())
		{
			String line = scan.nextLine();
			if(line.charAt(0) == '(') myExp.add(line);
			else // Print atoms
			{
				String[] c = line.split(" ");
				if(i%8 == 0) atoms = atoms + "\n" + c[0] + " = " + c[1];
				else atoms = atoms + "\t" + c[0] + " = " + c[1]; 
			}

			i++;
		}

		System.out.println(atoms);

		int num = 1;
		Scanner read = new Scanner(System.in);

		for(String y : myExp) 
		{ // Evaluate the expression
			System.out.println("Continue? Y or N");
			if(read.next().equalsIgnoreCase("n")) System.exit(0);

			ParseError p = new ParseError(y);
			if(p.isError()) System.out.println("\t\tError: \n" + "\t\t" + y + " " + p.errorString);
			else 
			{
				Expression x = new Expression(y);
				System.out.println(num + ".\t" + y + ": " + x.evaluate());
				num++;
			}
		}
	}
}


