package slavsquatsuperstar.demos.spacegame.combat.projectiles;

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

    public static final int NUM_PROJECTILES = 4;

    public static final SpriteSheet PROJECTILE_SPRITES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/projectiles.png",
            16, 16, NUM_PROJECTILES, 0);

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

    // TODO make weapon class
    public float getFireCooldown() {
        return fireCooldown;
    }

    // Factory Method

    // TODO add spawn offset
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
                addComponent(PROJECTILE_SPRITES.getSprite(spriteIndex));

                addComponent(new BallCollider(colliderSize).setTrigger(true));
                addComponent(new Rigidbody(0.001f));
            }
        };
    }

}
