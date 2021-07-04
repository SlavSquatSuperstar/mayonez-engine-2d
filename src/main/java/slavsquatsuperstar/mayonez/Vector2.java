package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.util.MathUtils;

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
     * Adds another vector to this vector.
     *
     * @param v another 2D vector
     * @return the vector sum
     */
    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    /**
     * Calculates the angle in degrees between this vector and another.
     *
     * @return the angle
     */
    public float angle(Vector2 v) {
        return (float) Math.toDegrees(Math.acos(this.dot(v) / this.length() / v.length()));
    }

    /**
     * Calculates the angle in degrees between this vector and the x-axis.
     *
     * @return the angle.
     */
    public float angle() {
        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Clamps the length of this vector if it exceeds the provided value,
     * while keeping the same direction. Useful for movement scripts.
     *
     * @param length any number
     * @return the clamped vector
     */
    public Vector2 clampLength(float length) {
        if (lengthSquared() > length * length) // too long
            return this.mul(length / length());
        return this;
    }

    /**
     * Returns the components of this vector in array form. Useful for looping.
     *
     * @return an array <code>{x, y}</code>
     */
    public float[] components() {
        return new float[]{x, y};
    }

    /**
     * Returns the z-component of the cross product between this vector and another.
     *
     * @param v another vector
     * @return the 2D cross product
     */
    public float cross(Vector2 v) {
        return this.x * v.y - this.y * v.x;
    }

    public float distanceSquared(Vector2 v) {
        return (this.x - v.x) * (this.x - v.x) + (this.y - v.y) * (this.y - v.y);
    }

    public float distance(Vector2 v) {
        return (float) Math.sqrt(distanceSquared(v));
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
        return this.mul(1 / scalar);
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
     * Multiplies the corresponding components of this vector and another vector.
     *
     * @param v another vector
     * @return the dot product
     */
    public float dot(Vector2 v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    /**
     * Calculates the length of this vector.
     *
     * @return this vector's length
     */
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Calculates the length squared of this vector (less CPU expensive than square
     * root).
     *
     * @return this vector's length squared
     */
    public float lengthSquared() {
        return (x * x) + (y * y);
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
     * Projects this vector onto another vector.
     *
     * @param vOnto another vector
     * @return the projected vector
     */
    public Vector2 project(Vector2 vOnto) {
        return vOnto.mul(this.dot(vOnto) / vOnto.lengthSquared());
    }

    /**
     * Rotates this vector by an angle around some origin.
     *
     * @param degrees the angle, in degrees
     * @param origin  the point to rotate around
     * @return the rotated vector
     */
    public Vector2 rotate(float degrees, Vector2 origin) {
        // Translate the vector space to the origin (0, 0)
        float x = this.x - origin.x;
        float y = this.y - origin.y;

        // Rotate the point around the new origin
        float cosTheta = (float) Math.cos(Math.toRadians(degrees));
        float sinTheta = (float) Math.sin(Math.toRadians(degrees));

        float newX = (x * cosTheta) - (y * sinTheta);
        float newY = (x * sinTheta) + (y * cosTheta);

        // Revert the vector space to the old point
        return new Vector2(newX, newY).add(origin);
    }

    /**
     * Sets this vector's components to the provided x and y coordinates.
     * Note: this method is a mutating method!
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets this vector's components to the given vector's components.
     * Note: this method is a mutating method!
     */
    public void set(Vector2 v) {
        set(v.x, v.y);
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
     * Calculates the vector the same direction as this vector and a magnitude of 1.
     * Returns (0, 0) if this vector is (0, 0).
     *
     * @return the unit vector
     */
    public Vector2 unitVector() {
        if (MathUtils.equals(lengthSquared(), 0))
            return this;
        return this.div(length());
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
