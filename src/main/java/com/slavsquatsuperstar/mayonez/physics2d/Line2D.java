package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.util.MathUtil;

public class Line2D {

	private Vector2 start, end;

	public Line2D(Vector2 start, Vector2 end) {
		this.start = start;
		this.end = end;
	}

	public Vector2 start() {
		return start;
	}

	public Vector2 end() {
		return end;
	}

	public Vector2 toVector() {
		return end.sub(start);
	}

	public boolean contains(Vector2 point) {
		float slope = (end.y - start.y) / (end.x - start.x);
		if (Float.isInfinite(slope)) { // vertical line (just compare x-values)
			return MathUtil.equals(point.x, start.x);
		} else {
			float yInt = end.y - (slope * end.x);
			return MathUtil.equals(point.y, yInt + point.x * slope);
		}
	}

	@Override
	public String toString() {
		return String.format("Start: %s, End: %s", start, end);
	}
}
