import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * mainSystem - A utility class for file operations, data processing, and mathematical computations.
 * 
 * This class provides methods for creating and managing files, generating salting values, 
 * smoothing data, and performing mathematical operations such as calculating areas under curves.
 * It is designed to work with CSV files and supports user interaction through the console.
 * 
 * Features:
 * - File creation, reading, and writing
 * - Salting and smoothing algorithms
 * - User input handling for bounds and file names
 * - Mathematical computations with formatting
 * 
 * Author: Arnab Das Utsa
 * Date: 04/30/2025
 */

public class mainSystem {

	/**
     * Scanner for user input.
     */
	Scanner scan = new Scanner(System.in);

	/**
     * Creates a new file with the specified name.
     * 
     * @param name The name of the file to create.
     * @return The created File object.
     */
	public File createFile(String name) {
		name = fixName(name);
		File file = new File(name);
		return file;
	}
	
	/**
     * Prompts the user to enter the lower bound, upper bound, and increment rate.
     * 
     * @return An array of Double containing the bounds and increment rate.
     */
		public Double[] getBounds() {
		Double[] bounds = new Double[3];
		
		System.out.println("Enter the lower bound of the range");
		double low = scan.nextDouble();
		System.out.println("Enter the upper bound of the range");
		double upper = scan.nextDouble();
		System.out.println("Enter the increment rate");
		double inc = scan.nextDouble();

		bounds[0] = low;
		bounds[1] = upper;
		bounds[2] = inc;
		
		return bounds;
	}

	/**
     * Creates a BufferedWriter for writing to a file.
     * 
     * @param name The name of the file to write to.
     * @return A BufferedWriter object, or null if an error occurs.
     */
	public BufferedWriter writeFile(String name) {
		try {
			File file = createFile(name);
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			return bufferedWriter;
		} catch (IOException e) {
			System.out.println("An error occured during the creation of salted file");
			return null;
		}
	}

	/**
     * Creates a BufferedReader for reading from a file.
     * 
     * @param name The name of the file to read from.
     * @return A BufferedReader object, or null if the file is not found.
     */
	public BufferedReader readFile(String name) {
		try {
			name = fixName(name);
			BufferedReader br = new BufferedReader(new FileReader(name));
			return br;
		} catch (IOException e) {
			System.out.println("File not found");
			return null;
		}
	}

	/**
     * Prompts the user to enter the name of a file.
     * 
     * @return The file name, formatted as a CSV file if necessary.
     */
	public String askName() {
		String fileName = scan.nextLine();
		fileName = fixName(fileName);
		return fileName;
	}

	/**
     * Ensures the file name has a ".csv" extension.
     * 
     * @param name The original file name.
     * @return The formatted file name.
     */
	public String fixName(String name) {
		if (!name.contains(".csv")) {
			name += ".csv";
		}
		return name;
	}

	 /**
     * Generates a salted value by randomly adding or subtracting a range.
     * 
     * @param salt The original value.
     * @param range The range to add or subtract.
     * @return The salted value.
     */
	public double salting(double salt, double range) {
		Random rng = new Random();
		int num = rng.nextInt(2);

		switch (num) {
		case 0:
			salt += range;
			break;
		case 1:
			salt -= range;
			break;
		default:
			break;
		}
		return salt;
	}
	
	/**
     * Smooths a list of data points using a moving average algorithm.
     * 
     * @param sm The list of data points to smooth.
     * @return A new list of smoothed data points.
     */
	public ArrayList<Double> smoothy(ArrayList<Double> sm){
		int count = 0;
		double avg;
		ArrayList<Double> average = new ArrayList<>();
		for (int i = 0; i < sm.size(); i++) {
			count = 1;
			avg = sm.get(i);
			
			for (int l = Math.max(0, i - 3); l < i; l++) {
				if (l <= (sm.size() - 1) && l >= 0) {
					avg += sm.get(l);
					count++;
				}
			}

			for (int u = i + 1; u <= Math.min(sm.size() - 1, i + 3); u++) {
				if (u <= 3 && u <= sm.size() - 1 || u < (sm.size() - 1)) {
					
					avg += sm.get(u);
					count++;
				}
			}

			average.add(format(avg/count));
		}
		return average;
	}


	/**
     * Counts the number of lines in a file.
     * 
     * @param name The name of the file.
     * @return The number of lines, or -1 if an error occurs.
     */
	public static int countLine(String name) {
		try (BufferedReader br = new BufferedReader(new FileReader(name))) {
			int lineCount = 0;
			while (br.readLine() != null) {
				lineCount++;
			}
			return lineCount;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
     * Formats a double value to three decimal places.
     * 
     * @param in The input value.
     * @return The formatted value.
     */
    
	public double format (double in) {
		
		DecimalFormat df = new DecimalFormat("0.###");
		
		String formatted = df.format(in);
		
		return Double.parseDouble(formatted);
	}

	/**
     * Calculates the area under a cosine curve for a given value.
     * 
     * @param cur The current value.
     * @param upper The upper bound.
     * @param isRad Whether the input is in radians.
     * @return The calculated area.
     */
	public double getArea(double cur, double upper, boolean isRad) {
		double rad;

			if (isRad == false) {
				rad = Math.toRadians(cur);
			} else rad = cur;;
			double area = Math.cos(rad);
			area = format(area);

		return area;
	}

}
