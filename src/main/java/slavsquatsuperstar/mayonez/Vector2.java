package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.util.MathUtils;

// TODO clamp magnitude, clamp values

/**
 * An object in 2D space that has magnitude and direction. Also represents a
 * point (x, y). Note: all methods, except for set() are non-mutating!
 */
public class Vector2 {

    public float x, y;

    /**
     * Initialize this vector to (0, 0).
     */
    public Vector2() {
        this(0f, 0f);
    }

    /**
     * Initialize this vector from an x and y value.
     */
    public Vector2(float x, float y) {
        set(x, y);
    }

    /**
     * Initialize this vector to another vector's x and y values.
     */
    public Vector2(Vector2 v) {
        this(v.x, v.y);
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
     * Adds another vector to this vector.
     *
     * @param v another 2D vector
     * @return the vector sum
     */
    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param v another 2D vector
     * @return the vector difference
     */
    public Vector2 sub(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    /**
     * Multiplies both components vector by a number.
     *
     * @param scalar any number
     * @return the multiplied vector
     */
    public Vector2 mul(float scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    /**
     * Multiplies the components of this vector by the corresponding components of another vector.
     *
     * @param v another vector
     * @return the multiplied vector
     */
    public Vector2 mul(Vector2 v) {
        return new Vector2(this.x * v.x, this.y * v.y);
    }

    /**
     * Divides both components vector by a number.
     *
     * @param scalar any non-zero number
     * @return the divided vector
     */
    public Vector2 div(float scalar) {
        if (scalar == 0) {
            Logger.log("Vector2: Attempted division by 0");
            return this.mul(0);
        }
        return new Vector2(x / scalar, y / scalar);
    }

    /**
     * Divided the components of this vector by the corresponding components of another vector.
     *
     * @param v another vector with non-zero components
     * @return the divided vector
     */
    public Vector2 div(Vector2 v) {
        return new Vector2(this.x / v.x, this.y / v.y);
    }

    /**
     * Projects this vector onto another vector.
     *
     * @param v another vector
     * @return the projected vector
     */
    public Vector2 project(Vector2 v) {
        return v.mul(this.dot(v) / v.lengthSquared());
    }

    // Scalar Operations

    /**
     * Multiplies the corresponding components of this vector and another vector.
     *
     * @param v another vector
     * @return the dot product
     */
    public float dot(Vector2 v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    // Properties

    /**
     * Calculates the length squared of this vector (less CPU expensive than square
     * root).
     *
     * @return the length squared of this vector
     */
    public float lengthSquared() {
        return (x * x) + (y * y);
    }

    /**
     * Calculates the length of this vector.
     *
     * @return the magnitude
     */
    public float magnitude() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Calculates the vector the same direction as this vector and a magnitude of 1.
     * Returns (0, 0) if this vector is (0, 0).
     *
     * @return the unit vector
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
     * Calculates the angle in degrees between this vector and another.
     *
     * @return the angle
     */
    public float angle(Vector2 v) {
        return (float) Math.toDegrees(Math.acos(this.dot(v) / this.magnitude() / v.magnitude()));
    }

    /**
     * Calculates the angle in degrees between this vector and the x-axis
     *
     * @return the angle.
     */
    public float angle() {
        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    public float[] components() {
        return new float[]{x, y};
    }

    // Transform Methods

    // Source: https://www.youtube.com/watch?v=dlUcIGnaAnk

    /**
     * Rotates this vector by an angle around some origin.
     * @param degrees the angle, in degrees
     * @param origin the point to rotate around
     * @return the rotated vector
     */
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
