package io.github.slavsquatsuperstar.mathlib;

public final class MathUtil {
	
	private MathUtil() {
	}

	// Clamp methods

	/**
	 * Restricts the given double' value to the provided bounds, inclusive.
	 * 
	 * @param lowerBound the lowest the value can be (floor)
	 * @param upperBound the highest the value can be (ceiling)
	 * @return the restricted double value
	 */
	public static double clamp(double toClamp, double lowerBound, double upperBound) {
		return (toClamp > upperBound ? upperBound : (toClamp < lowerBound ? lowerBound : toClamp));
	}

	/**
	 * Restricts the given integer's value to the provided bounds.
	 * 
	 * @param lowerBound the lowest the value can be (floor)
	 * @param upperBound the highest the value can be (ceiling)
	 * @return the restricted integer value
	 */
	public static int clamp(int toClamp, int lowerBound, int upperBound) {
		return (toClamp > upperBound ? upperBound : (toClamp < lowerBound ? lowerBound : toClamp));
	}

}
