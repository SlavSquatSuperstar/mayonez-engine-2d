package util;

public class Vector {

	public static final Vector POINT_VECTOR = new Vector(0, 0);

	public double x, y;

	public Vector(double x, double y) {
		this.x = x;
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
		return magnitude() == 0 ? Vector.POINT_VECTOR : scale(1 / magnitude());
	}

	@Override
	public String toString() {
		return String.format("<%f, %f>", x, y);
	}

}
