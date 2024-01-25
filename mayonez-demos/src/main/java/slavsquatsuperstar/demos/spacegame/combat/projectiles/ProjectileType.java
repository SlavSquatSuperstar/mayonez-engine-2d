package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * A type of projectile fired from a spaceship weapon possessing certain
 * characteristics.
 *
 * @author SlavSquatSuperstar
 */
public record ProjectileType(
        String name, float damage, float speed, float lifetime,
        float fireCooldown, float weaponSpread,
        Vec2 scale, Vec2 colliderSize,
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
