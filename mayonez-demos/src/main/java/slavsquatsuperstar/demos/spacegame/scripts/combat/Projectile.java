package slavsquatsuperstar.demos.spacegame.scripts.combat;

import mayonez.*;
import mayonez.graphics.Colors;
import mayonez.graphics.sprites.ShapeSprite;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.Collider;
import mayonez.scripts.KeepInScene;

/**
 * Allows a {@link mayonez.GameObject} to be launched with an initial velocity from a source object, and
 * allows it to damage other objects with a {@link Damageable} component.
 */
public class Projectile extends Script {

    private final GameObject source;
    private final float damage, speed;
    // TODO add lifetime

    public Projectile(GameObject source, float damage, float speed) {
        this.source = source;
        this.damage = damage;
        this.speed = speed;
    }

    @Override
    public void start() {
        gameObject.addTag("Projectile");
        if (getCollider() == null) Logger.debug("%s needs a collider to function!", this);
        if (getRigidbody() == null) Logger.debug("%s needs a rigidbody to function!", this);
        else getRigidbody().addVelocity(transform.getUp().mul(speed));
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public void onTriggerEnter(GameObject other) {
        // TODO tell physics to ignore collision
        if (other == source) return; // don't collide with source
        gameObject.setDestroyed();
//        if (other.hasTag("Damageable")) gameObject.setDestroyed();
    }

    /**
     * Creates a prefab projectile {@link mayonez.GameObject} from a projectile script and gives it a collider and rigidbody if
     * none are added.
     *
     * @param projectile the projectile script
     * @param name       the object name
     * @param size       the object radius
     * @param components any components to be added
     * @return the projectile object.
     */
    public static GameObject createPrefab(Projectile projectile, String name, float size, Component... components) {
        Transform transform = projectile.source.transform;
        Rigidbody sourceRb = projectile.source.getComponent(Rigidbody.class);
        return new GameObject(name, new Transform(
                transform.getPosition().add(transform.getUp().mul(0.5f)), transform.getRotation(), new Vec2(size)
        )) {
            @Override
            protected void init() {
                boolean hasRb = false;
                boolean hasCol = false;
                for (Component c : components) {
                    addComponent(c);
                    if (c instanceof Rigidbody) hasRb = true;
                    else if (c instanceof Collider) hasCol = true;
                }
                if (!hasRb) { // set velocity relative to object
                    Rigidbody rb;
                    addComponent(rb = new Rigidbody(0.01f));
                    if (sourceRb != null) rb.setVelocity(sourceRb.getVelocity());
                }
                if (!hasCol) {
                    addComponent(new BallCollider(new Vec2(1f)).setTrigger(true));
                    addComponent(new ShapeSprite(Colors.WHITE, true));
                }
                addComponent(new KeepInScene(KeepInScene.Mode.DESTROY));
                addComponent(projectile);
            }
        };
    }

}
