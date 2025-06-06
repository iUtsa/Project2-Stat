import java.io.BufferedWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
//import java.lang.Math;


/**
 * plotter - A program to calculate and store cosine function values for a given range.
 * 
 * This class uses the `mainSystem` utility class to perform file operations, format values, 
 * and calculate areas under the cosine curve. It prompts the user for input, calculates 
 * cosine values for a specified range, and stores the results in a CSV file.
 * 
 * Features:
 * - User input handling for file name, range bounds, and increment rate
 * - Support for both radians and degrees
 * - Calculation of cosine values for a range of inputs
 * - Storage of results in a CSV file
 * 
 * Author: Arnab Das Utsa
 */
public class plotter {

	public static void main(String[] args) {

		// Formala: Cos(x)
		// Initialize variabels and parameteres as needed
		mainSystem use = new mainSystem();
		String fileName;
		boolean isRad = false;
//		ArrayList<Double> x = new ArrayList<>();
//		ArrayList<Double> y = new ArrayList<>();
		StringBuilder text = new StringBuilder();
		double insurance = 0.000001; // I notice when the increment is 0.1,
		// 19.9 is considered equal to 20, so I added a small extra to have it include
		// 20.

		try {
			System.out.println("Cosine function calculation of integers\n");
			Scanner in = new Scanner(System.in);
			System.out.println("Enter file name to store data");
			fileName = use.askName();

			System.out.println("Are the values \n(1)radians \n(2)angle?");
			int val = in.nextInt();
			if (val == 1) {
				isRad = true;
			}
			System.out.println("Enter the lower bound of the range");
			double low = in.nextDouble();
			System.out.println("Enter the upper bound of the range");
			double upper = in.nextDouble();
			System.out.println("Enter the increment rate");
			double inc = in.nextDouble();

			// loop through the range
			// Then add the value of x and y to text
			while (low <= upper + insurance) {
				text.append(use.format(low)).append(", ").append(use.getArea(low, upper, isRad))
						.append("\n");
				low += inc;
			}
			BufferedWriter write = use.writeFile(fileName);
			write.write("x, cos(x)\n");
			// Write the data to designated file
			write.write(text.toString());
			System.out.println("Data has been stored in " + fileName);
			in.close();
			write.close();
		}

		catch (InputMismatchException e) {
			System.out.println("You have entered a non-number variable");
		}

		catch (IOException e) {
			System.out.println("An error occured.");
			e.printStackTrace();
		}

	}

}