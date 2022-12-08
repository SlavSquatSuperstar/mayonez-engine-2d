package slavsquatsuperstar.demos.spacegame.scripts;

import mayonez.*;
import mayonez.graphics.Colors;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.Collider;
import mayonez.scripts.KeepInScene;

public class Projectile extends Script {

    private final GameObject source;
    private final float damage, speed;

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
        else getRigidbody().setVelocity(transform.getUp().mul(speed));
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

    public static GameObject createPrefab(Projectile projectile, String name, float size, Component... components) {
        Transform transform = projectile.source.transform;
        return new GameObject(name, new Transform(
                transform.position.add(transform.getUp()), transform.rotation, new Vec2(size)
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
                if (!hasRb) addComponent(new Rigidbody(0.01f));
                if (!hasCol) addComponent(new BallCollider(new Vec2(1f)).setDebugDraw(Colors.WHITE, true).setTrigger(true));
                addComponent(new KeepInScene(KeepInScene.Mode.DESTROY));
                addComponent(projectile);
            }
        };
    }

}
