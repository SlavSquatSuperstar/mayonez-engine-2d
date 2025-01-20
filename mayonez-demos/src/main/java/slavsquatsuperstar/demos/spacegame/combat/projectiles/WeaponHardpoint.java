package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * A mounting position for a spaceship weapon.
 *
 * @author SlavSquatSuperstar
 */
public record WeaponHardpoint(Vec2 offset, float angle) {

    // Create from record object
    public WeaponHardpoint(Record record) {
        this(new Vec2(record.getFloat("xOffset"), record.getFloat("yOffset")),
                record.getFloat("angle"));
    }

}
