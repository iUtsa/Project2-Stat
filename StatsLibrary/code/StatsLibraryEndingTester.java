/**
 * This class tests various calculations implemented in the StatsLibraryEnding class. 
 * It includes tests for the Poisson distribution, Uniform distribution, and Chebyshev's theorem.
 * 
 * @author Arnab Das Utsa
 * @version 4/29/2025
 */
public class StatsLibraryEndingTester {

	public static void main(String[] args) {
		
		StatsLibraryEnding test = new StatsLibraryEnding();
		
		double testerResults1 = test.findPoisson(2.3, 4);
		double testerResults2 = test.findExpectedPoisson(2.3);
		double testerResults3 = test.findVariancePoisson(2.3);
		double testerResults4 = test.findChebyshev(2.7);
		double testerResults5 = test.findUniform(.2, .7);
		double testerResults6 = test.findExpectedUniform(.2, .7);
		double testerResults7 = test.findVarianceUniform(.2, .7);
		
		System.out.println("This is the calculation of the Poisson Distribution: " + testerResults1);
		System.out.println("This is the calculation of the Expected of the Poisson Distribution: " + testerResults2);
		System.out.println("This is the calculation of the Variance the Poisson Distribution: " + testerResults3);
		
		System.out.println("This is the calculation of Chebyshev's Theorem: " + testerResults4);
		
		System.out.println("This is the calculation of the Uniform Distribution: " + testerResults5);
		System.out.println("This is the calculation of the Expected of the Uniform Distribution: " + testerResults6);
		System.out.println("This is the calculation of the Variance the Uniform Distribution: " + testerResults7);
		
	}
	
}