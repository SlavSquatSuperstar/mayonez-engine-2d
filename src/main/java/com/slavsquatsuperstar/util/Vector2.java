package com.slavsquatsuperstar.util;

public class Vector2 {

	public static final Vector2 ZERO = new Vector2(0, 0);
	public static final Vector2 ONE = new Vector2(1, 1);

	public static final Vector2 UP = new Vector2(0, -1);
	public static final Vector2 DOWN = new Vector2(0, 1);
	public static final Vector2 LEFT = new Vector2(-1, 0);
	public static final Vector2 RIGHT = new Vector2(1, 0);

	public float x, y;

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	// Setters

	public void set(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
	}

	public Vector2 negate() {
		return this.multiply(-1f);
	}

	// Vector Operations

	public Vector2 add(Vector2 v) {
		return new Vector2(this.x + v.x, this.y + v.y);
	}

	public Vector2 subtract(Vector2 v) {
		return new Vector2(this.x - v.x, this.y - v.y);
	}

	public Vector2 multiply(float scaleFactor) {
		return new Vector2(x * scaleFactor, y * scaleFactor);
	}

	public Vector2 divide(float scaleFactor) {
		if (scaleFactor == 0)
			Logger.log("Vector2: Attempted division by 0");
		return new Vector2(x / scaleFactor, y / scaleFactor);
	}

	// Scalar Operations

	public float dot(Vector2 v) {
		return (this.x * v.x) + (this.y * v.y);
	}

	// Properties

	public float magnitude() {
		return (float) Math.sqrt((x * x) + (y * y));
	}

	public Vector2 unitVector() {
		return magnitude() == 0f ? ZERO : divide(magnitude());
	}

	public float angleDeg() {
		return (float) Math.toDegrees(Math.atan2(y, x));
	}

	public float angleRad() {
		return (float) Math.atan2(y, x);
	}

	@Override
	public String toString() {
		return String.format("(%.4f, %.4f)", x, y);
	}

}
