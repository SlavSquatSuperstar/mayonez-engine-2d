package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.combat.*;
import mayonez.util.*;

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
        var sourceXf = projectile.getSource().transform;
        var sourceRb = projectile.getSource().getComponent(Rigidbody.class);
        return new GameObject(name, new Transform(
                sourceXf.getPosition().add(sourceXf.getUp().mul(0.5f)), sourceXf.getRotation(), new Vec2(size)
        )) {
            @Override
            protected void init() {
                var hasRb = false;
                var hasCol = false;
                for (var comp : components) {
                    addComponent(comp);
                    if (comp instanceof Rigidbody) hasRb = true;
                    else if (comp instanceof Collider) hasCol = true;
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
        ).setZIndex(ZIndex.PROJECTILE);
    }

    public static GameObject createPlasma(GameObject source) {
        return createPrefab(
                new Projectile(source, 1.5f, 20f, 5f),
                "Plasma", 0.3f,
                new BallCollider(new Vec2(1f)).setTrigger(true),
                new ShapeSprite(new Color(0, 191, 255), true) // HTML Deep Sky Blue, #00BFFF
        ).setZIndex(ZIndex.PROJECTILE);
    }

}
