package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.graphics.debug.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.util.*;
import slavsquatsuperstar.demos.spacegame.ZIndex;

/**
 * Creates prefab projectiles that spaceships can fire.
 *
 * @author SlavSquatSuperstar
 */
public final class ProjectilePrefabs {

    private static final SpriteSheet PROJECTILE_SPRITES = Sprites.createSpriteSheet(
            "assets/textures/spacegame/projectiles.png",
            16, 16, 2, 0);

    private ProjectilePrefabs() {
    }

    /**
     * Creates a prefab projectile {@link mayonez.GameObject} from a projectile script and gives it a collider and rigidbody if
     * none are added.
     *
     * @param projectile the projectile script
     * @param name       the object name
     * @param size       the object size
     * @param components any components to be added
     * @return the projectile object.
     */
    public static GameObject createPrefab(Projectile projectile, String name, Vec2 size, Component... components) {
        var sourceXf = projectile.getSource().transform;
        var sourceRb = projectile.getSource().getComponent(Rigidbody.class);
        return new GameObject(name, new Transform(
                sourceXf.getPosition().add(sourceXf.getUp().mul(0.5f)),
                sourceXf.getRotation(), size
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
                addComponent(projectile);
            }
        };
    }

    public static GameObject createLaser(GameObject source) {
        return createPrefab(
                new Projectile(source, 1, 25f, 5f),
                "Laser", new Vec2(0.4f),
                new BallCollider(new Vec2(3 / 8f, 1f)).setTrigger(true),
                PROJECTILE_SPRITES.getSprite(0)
        ).setZIndex(ZIndex.PROJECTILE);
    }

    public static GameObject createPlasma(GameObject source) {
        return createPrefab(
                new Projectile(source, 1.5f, 20f, 5f),
                "Plasma", new Vec2(0.3f),
                new BallCollider(new Vec2(1f)).setTrigger(true),
                PROJECTILE_SPRITES.getSprite(1)
        ).setZIndex(ZIndex.PROJECTILE);
    }

}
