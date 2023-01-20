package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.Component;
import mayonez.GameObject;
import mayonez.Transform;
import mayonez.graphics.Colors;
import mayonez.graphics.sprites.ShapeSprite;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BallCollider;
import mayonez.physics.colliders.Collider;
import mayonez.scripts.combat.Projectile;

/**
 * Different prefab projectiles that spaceships can fire.
 *
 * @author SlavSquatSuperstar
 */
public class Projectiles {

    private Projectiles() {
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
        Transform transform = projectile.getSource().transform;
        Rigidbody sourceRb = projectile.getSource().getComponent(Rigidbody.class);
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
//                addComponent(new KeepInScene(KeepInScene.Mode.DESTROY));
                addComponent(projectile);
            }
        };
    }

    public static GameObject createLaser(GameObject source) {
        return createPrefab(
                new Projectile(source, 1, 25f, 5f),
                "Laser", 0.2f,
                new BallCollider(new Vec2(1f)).setTrigger(true),
                new ShapeSprite(Colors.RED, true)
        ).setZIndex(ZIndex.PROJECTILE.zIndex);
    }

    public static GameObject createPlasma(GameObject source) {
        return createPrefab(
                new Projectile(source, 1.5f, 20f, 5f),
                "Plasma", 0.3f,
                new BallCollider(new Vec2(1f)).setTrigger(true),
                new ShapeSprite(Colors.SKY_BLUE, true)
        ).setZIndex(ZIndex.PROJECTILE.zIndex);
    }

}
