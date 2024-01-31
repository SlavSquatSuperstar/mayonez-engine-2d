package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * Defines characteristics for a type of projectile fired from a weapon.
 *
 * @author SlavSquatSuperstar
 */
public record ProjectileType(
        // Projectile Stats
        String name, float damage, float speed, float lifetime,
        // Weapon Handling
        float fireCooldown, float weaponSpread,
        // Projectile Size
        Vec2 scale, Vec2 colliderSize,
        // Projectile Sprite
        int spriteIndex
) {

    // Create from record object
    public ProjectileType(Record record) {
        this(
                record.getString("name"), record.getFloat("damage"),
                record.getFloat("speed"), record.getFloat("lifetime"),

                record.getFloat("fireCooldown"), record.getFloat("weaponSpread"),

                new Vec2(record.getFloat("scale")),
                new Vec2(record.getFloat("colliderSizeX"), record.getFloat("colliderSizeY")),

                record.getInt("spriteIndex")
        );
    }

}
