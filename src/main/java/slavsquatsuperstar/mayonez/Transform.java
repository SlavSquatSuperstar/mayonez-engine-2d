package slavsquatsuperstar.mayonez;

/**
 * Stores the position, rotation and scale of a GameObject and provides additional methods.
 *
 * @author SlavSquatSuperstar
 */
public class Transform { // TODO extend component?

    /**
     * Where the object is located in the scene.
     */
    public Vector2 position = new Vector2();
    /**
     * How much the object is being stretched along its axes.
     */
    public Vector2 scale = new Vector2(1, 1);
    /**
     * The angle in degrees the object is oriented.
     */
    public float rotation = 0;

    public Transform() {}

    public Transform(Vector2 position) {
        this.position = position;
    }

    /**
     * Translates the parent object along the x and y axes.
     *
     * @param displacement a vector
     */
    public void move(Vector2 displacement) {
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

    @Override
    public String toString() {
        return String.format("Position: %s, Scale: %s", position, scale);
    }

}
