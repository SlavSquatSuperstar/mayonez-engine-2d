package slavsquatsuperstar.mayonez;

/**
 * Stores the position and scale of a GameObject and provides additional
 * methods.
 *
 * @author SlavSquatSuperstar
 */
public class Transform {

    public Vector2 position = new Vector2();
    public Vector2 scale = new Vector2(1, 1);
    public float rotation = 0;

    public Transform() {}

    public Transform(Vector2 position) {
        this.position = position;
    }

    public void move(Vector2 displacement) {
        position = position.add(displacement);
    }

    public void rotate(float degrees) {
        rotation += degrees;
    }

    @Override
    public String toString() {
        return String.format("Position: %s, Scale: %s", position, scale);
    }

}
