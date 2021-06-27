package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.util.MathUtils;

// TODO clamp magnitude, clamp values

/**
 * An object in 2D space that has magnitude and direction. Also represents a
 * point (x, y). Note: all methods, except for set() are non-mutating!
 */
public class Vector2 {

    public float x, y;

    public Vector2() {
        this(0f, 0f);
    }

    public Vector2(float x, float y) {
        set(x, y);
    }

    public Vector2(Vector2 v) {
        set(v.x, v.y);
    }

    /**
     * Shorthand for <code>this.x = x; this.y = y</code>.
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
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
        if (scaleFactor == 0) {
            Logger.log("Vector2: Attempted division by 0");
            return this.mul(0);
        }
        return new Vector2(x / scaleFactor, y / scaleFactor);
    }

    /**
     * Projects this vector onto another vector.
     * @param v another vector
     * @return the projected vector
     */
    public Vector2 project(Vector2 v) {
        return v.mul(this.dot(v) / v.lengthSquared());
    }

    // Scalar Operations

    /**
     * Multiplies the components of this vector and another vector.
     *
     * @param v another vector
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
     * Normalizes this vector, or calculates its unit vector.
     *
     * @return a vector with magnitude 1 and the same direction as this vector. Returns (0, 0) if this vector is (0, 0)
     */
    public Vector2 unit() {
        if (MathUtils.equals(lengthSquared(), 0))
            return this;
        else if (MathUtils.equals(lengthSquared(), 0))
            return this;
        else
            return this.div(magnitude());
    }

    /**
     * @return the angle between this vector and another.
     */
    public float angle(Vector2 v) {
        return (float) Math.toDegrees(Math.acos(this.dot(v) / this.magnitude() / v.magnitude()));
    }

    /**
     * @return the angle of this vector in degrees.
     */
    public float angle() {
        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    public float[] components() {
        return new float[] {x, y};
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
            return MathUtils.equals(this.x, v.x) && MathUtils.equals(this.y, v.y);
        }
        return super.equals(obj);
    }

    // TODO specify rounding
    @Override
    public String toString() {
        return String.format("(%.4f, %.4f)", x, y);
    }

}
