package util;

public class Vector2 {

	public double x, y;

	public Vector2() {
		this(0, 0);
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Scalar Operations

	public double dot(Vector2 v) {
		return (x * v.x) + (y * v.y);
	}

	public double magnitude() {
		return Math.sqrt((x * x) + (y * y));
	}

	// Vector Operations

	public Vector2 add(Vector2 v) {
		return new Vector2(x + v.x, y + v.y);
	}

	public Vector2 scale(double scalar) {
		return new Vector2(x * scalar, y * scalar);
	}

	public Vector2 unitVector() {
		return magnitude() == 0 ? new Vector2() : scale(1 / magnitude());
	}

	@Override
	public String toString() {
		return String.format("<%f, %f>", x, y);
	}

}
