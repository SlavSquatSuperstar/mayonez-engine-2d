package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.graphics.Colors;
import mayonez.graphics.sprite.ShapeSprite;
import mayonez.math.Vec2;
import mayonez.physics.colliders.BallCollider;
import slavsquatsuperstar.demos.spacegame.scripts.Projectile;

/**
 * Different prefab projectiles that spaceships can fire.
 *
 * @author SlavSquatSuperstar
 */
public class Projectiles {

    private Projectiles() {
    }

    public static GameObject createLaser(GameObject source) {
        return Projectile.createPrefab(
                new Projectile(source, 1, 25f),
                "Laser", 0.2f,
                new BallCollider(new Vec2(1f)).setTrigger(true),
                new ShapeSprite(Colors.RED, true)
        ).setZIndex(0);
    }

    public static GameObject createPlasma(GameObject source) {
        return Projectile.createPrefab(
                new Projectile(source, 1.5f, 20f),
                "Plasma", 0.3f,
                new BallCollider(new Vec2(1f)).setTrigger(true),
                new ShapeSprite(Colors.SKY_BLUE, true)
        ).setZIndex(0);
    }

}
