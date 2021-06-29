package slavsquatsuperstar.mayonez.physics2d;

@FunctionalInterface
public interface ForceGenerator {
    void applyForce(Rigidbody2D rb, float dt);
}
