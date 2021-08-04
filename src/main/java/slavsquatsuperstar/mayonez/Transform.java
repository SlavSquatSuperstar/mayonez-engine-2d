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

    // Mutator Methods

    /**
     * Translates the parent object along the x and y axes.
     *
     * @param displacement a vector
     * @return this transform
     */
    public Transform move(Vec2 displacement) {
        position = position.add(displacement);
        return this;
    }

    /**
     * Rotates the parent object around its center.
     *
     * @param degrees the angle
     * @return this transform
     */
    public Transform rotate(float degrees) {
        rotation += degrees;
        return this;
    }

    /**
     * Stretches the parent object and all its components by the given factors along the x and y axes
     *
     * @param dilation a vector
     * @return this transform
     */
    public Transform resize(Vec2 dilation) {
        scale = scale.mul(dilation);
        return this;
    }

    /**
     * Transforms a point from world space to the object's local space.
     *
     * @param world a 2D point in the world
     * @return the localized point
     */
    public Vec2 toLocal(Vec2 world) {
        return world.sub(position).div(scale).rotate(-rotation);
    }

    /**
     * Transforms a point from the object's local space to world space.
     *
     * @param local a localized 2D point
     * @return the point in the world
     */
    public Vec2 toWorld(Vec2 local) {
        return local.rotate(rotation).mul(scale).add(position);
    }

    @Override
    public String toString() {
        return String.format("Position: %s, Rotation: %.2f, Scale: %s", position, rotation, scale);
    }

}
