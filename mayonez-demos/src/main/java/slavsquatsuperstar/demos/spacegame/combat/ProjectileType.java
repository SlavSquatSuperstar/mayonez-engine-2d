package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A type of projectile fired from a spaceship weapon.
 *
 * @author SlavSquatSuperstar
 */
public class ProjectileType {

    private static final SpriteSheet PROJECTILE_SPRITES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/projectiles.png",
            16, 16, 2, 0);

    private final String name;
    private final float damage, speed, lifetime, fireCooldown;
    private final Vec2 scale, colliderSize;
    private final int spriteIndex;

    public ProjectileType(Record record) {
        name = record.getString("name");

        damage = record.getFloat("damage");
        speed = record.getFloat("speed");
        lifetime = record.getFloat("lifetime");
        fireCooldown = record.getFloat("fireCooldown");

        scale = new Vec2(record.getFloat("scale"));
        colliderSize = new Vec2(record.getFloat("colliderSizeX"),
                record.getFloat("colliderSizeY"));

        spriteIndex = record.getInt("spriteIndex");
    }

    // Getters

    public float getFireCooldown() {
        return fireCooldown;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    // Factory Method

    public GameObject createProjectileObject(GameObject source) {
        var sourceXf = source.transform;
        var projXf = new Transform(
                sourceXf.getPosition().add(sourceXf.getUp().mul(0.5f)),
                sourceXf.getRotation(), scale
        );

        return new GameObject(name, projXf, SpaceGameZIndex.PROJECTILE) {
            @Override
            protected void init() {
                setLayer(getScene().getLayer(SpaceGameLayer.PROJECTILES));
                addComponent(new Projectile(source, damage, speed, lifetime));
                addComponent(new BallCollider(colliderSize).setTrigger(true));
                addComponent(PROJECTILE_SPRITES.getSprite(spriteIndex));

                // Set initial velocity
                Rigidbody rb;
                addComponent(rb = new Rigidbody(0.01f));
                var sourceRb = source.getComponent(Rigidbody.class);
                if (sourceRb != null) rb.setVelocity(sourceRb.getVelocity());
            }
        };
    }

}
