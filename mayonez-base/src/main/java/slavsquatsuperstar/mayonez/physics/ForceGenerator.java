package slavsquatsuperstar.mayonez.physics;

/**
 * A push or pull that acts upon an object.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface ForceGenerator {
    void applyForce(Rigidbody rb, float dt);
}
