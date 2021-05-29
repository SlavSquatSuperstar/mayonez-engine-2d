package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.util.MathUtil;

public class Vector2 {

    public float x, y;

    public Vector2() {
        this(0f, 0f);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    // Vector Operations

    /**
     * Adds another vector from this vector
     *
     * @param v another 2D vector.
     * @return the sum of this vector and the other.
     */
    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param v another 2D vector.
     * @return the difference of this vector and the other vector.
     */
    public Vector2 sub(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    /**
     * Scales this vector by a number.
     *
     * @param scaleFactor any number.
     * @return the scalar multiplication of this vector and the factor.
     */
    public Vector2 mul(float scaleFactor) {
        return new Vector2(x * scaleFactor, y * scaleFactor);
    }

    /**
     * Scales this vector inversely by a number.
     *
     * @param scaleFactor any number.
     * @return the scalar division of this vector and the factor.
     */
    public Vector2 div(float scaleFactor) {
        if (scaleFactor == 0)
            Logger.log("Vector2: Attempted division by 0");
        return new Vector2(x / scaleFactor, y / scaleFactor);
    }

    // Scalar Operations

    /**
     * Multiplies the components of this vector and another vector.
     *
     * @param v another 2D vector
     * @return the dot product of this vector and the other vector.
     */
    public float dot(Vector2 v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    // Properties

    /**
     * Calculates the length squared of this vector (less CPU expensive than square
     * root).
     *
     * @return the length squared of this vector.
     */
    public float lengthSquared() {
        return (x * x) + (y * y);
    }

    /**
     * Calculates the length of this vector.
     *
     * @return the magnitude of this vector.
     */
    public float magnitude() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes this vector.
     *
     * @return a vector with magnitude 1 and the same direction as this vector.
     * @return (0, 0) if the magnitude of this vector is 0
     */
    public Vector2 unit() {
        return (MathUtil.equals(magnitude(), 0)) ? this : this.div(magnitude());
    }

    /**
     * @return the angle of this vector in degrees.
     */
    public float angleDeg() {
        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    // Transform Methods

    // Source: https://www.youtube.com/watch?v=dlUcIGnaAnk
    public Vector2 rotate(float degrees, Vector2 origin) {
        // Translate the vector space to the origin (0, 0)
        float x = this.x - origin.x;
        float y = this.y - origin.y;

        // Rotate the point around the new origin
        float cos = (float) Math.cos(Math.toRadians(degrees));
        float sin = (float) Math.sin(Math.toRadians(degrees));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (y * sin) + (x * sin);

        // Revert the vector space to the old point
        return new Vector2(xPrime, yPrime).add(origin);
    }

    // Overrides

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2) {
            Vector2 v = (Vector2) obj;
            return MathUtil.equals(this.x, v.x) && MathUtil.equals(this.y, v.y);
        }
        return super.equals(obj);
    }

    // TODO specify rounding, matrix form
    @Override
    public String toString() {
        return String.format("(%.4f, %.4f)", x, y);
    }

}
