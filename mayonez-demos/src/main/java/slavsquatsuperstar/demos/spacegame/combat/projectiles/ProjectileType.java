package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.math.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.PrefabUtils;

/**
 * Defines characteristics for a type of projectile fired from a weapon.
 *
 * @param name         the projectile's name
 * @param damage       the projectile's damage
 * @param speed        the projectile's speed in units per second
 * @param lifetime     the projectile's lifetime in seconds
 * @param fireCooldown the projectile's fire cooldown in second
 * @param weaponSpread the projectile's spread in degrees
 * @param scale        the projectile's scale
 * @param colliderSize the projectile's relative hitbox size
 * @param sweepFactor  the projectile's hitbox sweep factor
 * @param spriteIndex  the projectile's sprite index
 * @author SlavSquatSuperstar
 */
// TODO separate projectile and weapon
public record ProjectileType(
        // Projectile Stats
        String name, float damage, float speed, float lifetime,
        // Weapon Handling
        float fireCooldown, float weaponSpread,
        // Projectile Size
        Vec2 scale, Vec2 colliderSize, float sweepFactor,
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
                PrefabUtils.getColliderSize(record),
                record.getFloat("sweepFactor"),
                record.getInt("spriteIndex")
        );
    }

}
