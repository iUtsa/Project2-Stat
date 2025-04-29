import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * salter - A program to apply a "salting" operation to data from a CSV file.
 * 
 * This class uses the `mainSystem` utility class to perform file operations, 
 * format values, and apply random salting to the y-values of data points in a CSV file. 
 * The salted data is then stored in a new CSV file.
 * 
 * Features:
 * - Reads data from an input CSV file
 * - Applies a random salting operation to y-values within a specified range
 * - Writes the salted data to a new CSV file
 * - Handles file input/output errors gracefully
 * 
 * Author: Arnab Das Utsa
 */
public class salter {

	public static void main(String[] args) {

		// Initialize variables and parameters as needed

		mainSystem use = new mainSystem();
		String name;
		String line;
		StringBuilder build = new StringBuilder();
		String[] list = new String[1];
		Scanner in = new Scanner(System.in);
		double x, salt;

		// Enter name of file and correct the format
		System.out.println("Enter file name");
		
		name = use.askName();
		
		

		// Asking for the range of salting
		System.out.println("Enter a number for the salting range:");
		double range = Math.abs(in.nextDouble());

		// try and catch
		try {
			BufferedReader br = use.readFile(name);


			// Since I have a header, I need to skip first line
			// Probably could've been more efficient.
			boolean firstLine = true;

			while ((line = br.readLine()) != null) {

				list = line.split(",");

				if (firstLine) {
					build.append(line).append("\n");
					firstLine = false;
					continue;
				}
				salt = Double.parseDouble(list[1].trim());

				// Salting the result (y values)
				salt = use.salting(salt, range);
				x = Double.parseDouble(list[0].trim());
				x = use.format(x);
				
				salt = use.format(salt);
				build.append(x).append(", ").append(salt).append("\n");

			}
			String nameSalt = name.replace(".csv", "_salted.csv");
			BufferedWriter write = use.writeFile(nameSalt);
			write.write(build.toString());
			System.out.println("Sucessfully stored salted data in " + nameSalt);
			write.close();
			br.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("An error occured during the creation of salted file");
		}
		in.close();

	}
	

}
