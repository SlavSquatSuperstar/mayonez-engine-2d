package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * A mounting position for a spaceship weapon.
 *
 * @author SlavSquatSuperstar
 */
public record WeaponHardpoint(String name, Vec2 offset, float angle) {

    // Create from record object
    public WeaponHardpoint(Record record) {
        this(record.getString("name"),
                new Vec2(record.getFloat("xOffset"), record.getFloat("yOffset")),
                record.getFloat("angle"));
    }

}
