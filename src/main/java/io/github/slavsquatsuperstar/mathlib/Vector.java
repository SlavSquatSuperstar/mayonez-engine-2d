package io.github.slavsquatsuperstar.mathlib;

/**
 * Representation of an x- and y- value in the 2D plane. Note that Vectors are
 * immutable, like Strings, so make sure the assign a function's return value to
 * the variable to modify it.
 */
public class Vector {

	private double x, y;

	public Vector() {
		this(0, 0);
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Getter and Setter Methods

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	// Scalar Operations

	public double dot(Vector v) {
		return (x * v.x) + (y * v.y);
	}

	public double magnitude() {
		return Math.sqrt((x * x) + (y * y));
	}

	// Vector Operations

	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}

	public Vector scale(double scalar) {
		return new Vector(x * scalar, y * scalar);
	}

	public Vector unitVector() {
		return scale(1 / magnitude());
	}

	@Override
	public String toString() {
		return String.format("<%f, %f>", x, y);
	}

}
