package com.slavsquatsuperstar.mayonez.physics2d;

import com.slavsquatsuperstar.mayonez.Vector2;

public class CircleCollider extends Collider2D {

	private float radius;

	public CircleCollider(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	@Override
	public Vector2 center() {
		return rb.position().add(new Vector2(radius, radius));
	}

	@Override
	public boolean contains(Vector2 point) {
		return point.sub(center()).lengthSquared() <= radius * radius;
	}

	@Override
	// Projection
	// Source: https://www.youtube.com/watch?v=Yx1fo2YLJOs
	public boolean intersects(Line2D line) {
		// Return true if either endpoint is in the circle
		if (contains(line.start()) || contains(line.end()))
			return true;

		// Project the segment from start to center onto the original line
		Vector2 projectFrom = center().sub(line.start());
		Vector2 projectOnto = line.toVector();
		Vector2 projected = projectOnto.mul(projectFrom.dot(projectOnto) / projectOnto.lengthSquared());
		Vector2 closest = line.start().add(projected);
		return contains(closest);

//		// Get the percentage of the line segment shared with the circle
//		float t = cntrToStart.dot(toProject) / toProject.dot(toProject); // parameterized position
//		if (t < 0f || t > 1f)
//			return false;
//
//		// Find the point closest to the segment (endpoint of projected line)
//		Vector2 closest = line.start().add(toProject).mul(t);
//		return contains(closest);
	}

}
