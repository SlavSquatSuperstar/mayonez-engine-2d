package slavsquatsuperstar.mayonez.physics2d;

/**
 * A push or pull that acts upon an object.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface ForceGenerator {
    void applyForce(Rigidbody2D rb, float dt);
}
