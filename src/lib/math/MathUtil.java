package lib.math;

public class MathUtil {

	// Averaging methods

	/**
	 * @return the double average of given set of doubles.
	 */
	public static double average(double[] values) {
		double sum = 0;

		for (double value : values)
			sum += value;

		return sum / values.length;
	}

	/**
	 * @return the integer average of given set of integer.
	 */
	public static int average(int[] values) {
		int sum = 0;

		for (int value : values)
			sum += value;

		return sum / values.length;
	}

	// Data type conversion methods

	/**
	 * @return the integer value of the given boolean.
	 */
	public static int booleanToInt(boolean toConvert) {
		return (toConvert ? 1 : 0);
	}

	/**
	 * @return the double value of the given string.
	 */
	public static double stringToDouble(String toConvert) {
		return Double.parseDouble(toConvert);
	}

	/**
	 * @return the integer value of the given string.
	 */
	public static int stringToInt(String toConvert) {
		return Integer.parseInt(toConvert);
	}

	/**
	 * @return the string value of the given object.
	 */
	public static <T> String stringValue(T toConvert) {
		return String.valueOf(toConvert);
	}

	// Clamp methods

	/**
	 * Restricts the given double' value to the provided bounds, inclusive.
	 * 
	 * @param upperBound the highest the value can be (ceiling)
	 * @param lowerBound the lowest the value can be (floor)
	 * @return the restricted double value
	 */
	public static double clamp(double toClamp, double upperBound, double lowerBound) {
		return (toClamp > upperBound ? upperBound : (toClamp < lowerBound ? lowerBound : toClamp));
	}

	/**
	 * Restricts the given integer's value to the provided bounds.
	 * 
	 * @param upperBound the highest the value can be (ceiling)
	 * @param lowerBound the lowest the value can be (floor)
	 * @return the restricted integer value
	 */
	public static int clamp(int toClamp, int upperBound, int lowerBound) {
		return (toClamp > upperBound ? upperBound : (toClamp < lowerBound ? lowerBound : toClamp));
	}

	// Random number methods

	/**
	 * @param lowerBound the lowest value the method should return
	 * @param upperBound the highest value the method should return
	 * @return a random double value between the given bounds, inclusive.
	 */
	public static double random(double lowerBound, double upperBound) {
		return ((Math.random() * (upperBound - lowerBound)) + lowerBound);
	}

	/**
	 * @param bound the farthest away from 0 the return value should be
	 * @return a random double value between 0 and the given bound.
	 */
	public static double random(double bound) {
		return (bound >= 0 ? random(0.0, bound) : random(bound, 0.0));
	}

	/**
	 * @param lowerBound the lowest value the method should return
	 * @param upperBound the highest value the method should return
	 * @return a random integer value between the given bounds, inclusive.
	 */
	public static int random(int lowerBound, int upperBound) {
		return (int) random((double) --lowerBound, (double) ++upperBound);
//		return (int) ((Math.random() * (++upperBound - --lowerBound)) + lowerBound);
	}
	// (-1, 5)
	// random() * 7
	// (0, 6) - 1
	// (-1, 5)

	/**
	 * @param bound the farthest away from 0 the return value should be
	 * @return a random integer value between 0 and the given bound.
	 */
	public static int random(int bound) {
		return (bound >= 0 ? random(0, bound) : random(bound, 0));
	}

	/**
	 * @return a random boolean value.
	 */
	public static boolean randomBoolean() {
		return random(1) == 0;
	}

	/**
	 * @return a random decimal between 0 and 1, rounded to two decimal places.
	 */
	public static double randomPercent() {
		return round(random(0.0, 1.0), 2);
	}

	/**
	 * @return a random either 1 or -1.
	 */
	public static double randomSign() {
		return (random(0, 1) == 1) ? 1 : -1;
	}

	// Rounding Methods

	/**
	 * Rounds the given value to the specified amount of decimal places.
	 * 
	 * @return the rounded decimal value.
	 */
	public static double round(double toRound, int decimalPlaces) {
		double toAdd = 5 / Math.pow(10, decimalPlaces + 1);
		toRound += toAdd;

		return toRound = truncate(toRound, decimalPlaces);
	}

	/**
	 * Cuts off the given value at the specified amount of decimal places.
	 * 
	 * @return the truncated decimal value.
	 */
	public static double truncate(double toTruncate, int decimalPlaces) {
		return toTruncate = (int) (toTruncate * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
	}

}
