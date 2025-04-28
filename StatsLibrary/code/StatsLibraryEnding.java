import java.math.BigInteger;

/**
 * This class computes the Poisson distribution, Uniform distribution, and Chebyshev's theorem.
 * Expected values and variances are calculated where applicable.  
 * 
 * @author Arnab Das Utsa
 * @version 4/29/2025
 */
public class StatsLibraryEnding {

	/**
	 * Default constructor.
	 */
	public StatsLibraryEnding() {}

	/**
	 * Constructor that takes a string input.
	 */
	public StatsLibraryEnding(String input) {}

	/**
	 * Constructor that takes a double as input.
	 */
	public StatsLibraryEnding(double input) {}

	/**
	 * Calculates the factorial of a number using BigInteger for large numbers.
	 *
	 * @param n The number to compute the factorial of.
	 * @return The factorial of n as a BigInteger.
	 */
	public BigInteger findFactorial(int n) {
		BigInteger factorial = new BigInteger(Integer.toString(n));
		BigInteger zero = new BigInteger("0");
		BigInteger one = new BigInteger("1");
		if (factorial.equals(zero)) {
			factorial = one;
		} else {
			for (int i = n; i > 1; i--) {
				BigInteger index = new BigInteger(Integer.toString(i));
				BigInteger oneLessThanIndex = index.subtract(one);
				factorial = factorial.multiply(oneLessThanIndex);
			}
		}
		return factorial;
	}

	/**
	 * Computes the Poisson Distribution.
	 *
	 * @param l Lambda.
	 * @param y The number of events.
	 * @return The probability of observing exactly y events.
	 */
	public double findPoisson(double l, int y) {
		double e = Math.E;
		double factorialOfy = findFactorial(y).doubleValue();
		double numerator = (Math.exp(-l) * Math.pow(l, y));
		double poisson = numerator / factorialOfy;

		return poisson;
	}

	/**
	 * Calculates the expected value of a Poisson Distribution.
	 *
	 * @param l Lambda.
	 * @return The expected value.
	 */
	public double findExpectedPoisson(double l) {
		return l;
	}

	/**
	 * Calculates the variance of a Poisson Distribution.
	 *
	 * @param l The rate parameter (lambda) of the Poisson Distribution.
	 * @return The variance.
	 */
	public double findVariancePoisson(double l) {
		return l;
	}

	/**
	 * Applies Chebyshev's theorem to find the minimum probability that a random variable falls within k standard deviations of the mean.
	 *
	 * @param k The number of standard deviations from the mean.
	 * @return The probability according to Chebyshev's theorem.
	 */
	public double findChebyshev(double k) {
		double chebyshev = 1 - (1 / Math.pow(k, 2));

		return chebyshev;
	}

	/**
	 * Calculates the probability density function for a uniform distribution between two bounds.
	 *
	 * @param theta1 The lower bound of the distribution.
	 * @param theta2 The upper bound of the distribution.
	 * @return The density of the uniform distribution over the interval.
	 */
	public double findUniform(double theta1, double theta2) {
		double uniform = 1 / (theta2 - theta1);

		return uniform;
	}

	/**
	 * Calculates the expected value of a uniform distribution.
	 *
	 * @param theta1 The lower bound of the distribution.
	 * @param theta2 The upper bound of the distribution.
	 * @return The expected value of the distribution.
	 */
	public double findExpectedUniform(double theta1, double theta2) {
		double expected = (theta1 + theta2) / 2;

		return expected;
	}

	/**
	 * Calculates the variance of a uniform distribution.
	 *
	 * @param theta1 The lower bound of the distribution.
	 * @param theta2 The upper bound of the distribution.
	 * @return The variance of the distribution.
	 */
	public double findVarianceUniform(double theta1, double theta2) {
		double variance = Math.pow((theta2 - theta1), 2) / 12;

		return variance;
	}
}
