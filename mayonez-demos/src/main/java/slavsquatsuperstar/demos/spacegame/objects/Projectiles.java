package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.graphics.Colors;
import mayonez.math.Vec2;
import mayonez.physics.colliders.BallCollider;
import slavsquatsuperstar.demos.spacegame.scripts.Projectile;

/**
 * Different projectiles that spaceships can fire.
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
                new BallCollider(new Vec2(1f)).setDebugDraw(Colors.RED, true).setTrigger(true)
        ).setZIndex(0);
    }

    public static GameObject createPlasma(GameObject source) {
        return Projectile.createPrefab(
                new Projectile(source, 1.5f, 20f),
                "Plasma", 0.3f,
                new BallCollider(new Vec2(1f)).setDebugDraw(Colors.SKY_BLUE, true).setTrigger(true)
        ).setZIndex(0);
    }

}
