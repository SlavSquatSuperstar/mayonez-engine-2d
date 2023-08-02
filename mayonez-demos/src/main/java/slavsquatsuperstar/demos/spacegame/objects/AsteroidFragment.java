package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import mayonez.scripts.combat.*;

/**
 * A fragment of a destroyed asteroid.
 *
 * @author SlavSquatSuperstar
 */
public class AsteroidFragment extends GameObject {

    private final Vec2 position, size, velocity;
    private final int startingHealth;
    private final Color color;

    public AsteroidFragment(String name, Vec2 position, Vec2 size, Vec2 velocity, Color color) {
        super(name);
        setZIndex(ZIndex.ASTEROID);
        this.position = position;
        this.size = size;
        this.velocity = velocity;
        startingHealth = 2;
        this.color = color;
    }

    @Override
    protected void init() {
        setRandomTransform();
        addAsteroidCollider();
        addAsteroidStartingVelocity();
        addComponent(new Damageable(startingHealth));
        addComponent(new DestroyAfterDuration(Random.randomFloat(10, 15)));
    }

    private void setRandomTransform() {
        transform.setPosition(position);
        transform.setRotation(Random.randomFloat(0f, 360f));
        transform.setScale(size);
    }

    private void addAsteroidCollider() {
        addComponent(new BallCollider(new Vec2(1f)));
        addComponent(new KeepInScene(KeepInScene.Mode.WRAP));
        addComponent(new ShapeSprite(color, true));
    }

    private void addAsteroidStartingVelocity() {
        Rigidbody rb;
        addComponent(rb = new Rigidbody(startingHealth, 0.2f, 0.2f));
        rb.setVelocity(velocity);
    }
}
