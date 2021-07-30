package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.math.Vec2;

/**
 * Stores the position, rotation and scale of a GameObject and provides additional methods.
 *
 * @author SlavSquatSuperstar
 */
public class Transform { // TODO extend component?

    /**
     * Where the object is located in the scene.
     */
    public Vec2 position = new Vec2();
    /**
     * How much the object is being stretched along its axes.
     */
    public Vec2 scale = new Vec2(1, 1);
    /**
     * The angle in degrees the object is oriented.
     */
    public float rotation = 0;

    public Transform() {}

    public Transform(Vec2 position) {
        this.position = position;
    }

    /**
     * Translates the parent object along the x and y axes.
     *
     * @param displacement a vector
     */
    public void move(Vec2 displacement) {
        position = position.add(displacement);
    }

    /**
     * Rotates the parent object around its center.
     *
     * @param degrees the angle
     */
    public void rotate(float degrees) {
        rotation += degrees;
    }

    /**
     * Stretches the parent object and all its components by the given x and y axes
     * @param dilation a vector
     */
    public void resize(Vec2 dilation) {
        scale = scale.mul(dilation);
    }

    @Override
    public String toString() {
        return String.format("Position: %s, Scale: %s", position, scale);
    }

}
