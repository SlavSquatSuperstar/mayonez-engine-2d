package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.math.*;
import mayonez.util.Record;

/**
 * A set of properties for spaceship thrusters.
 *
 * @author SlavSquatSuperstar
 */
public record ThrusterProperties(
        ThrustDirection moveDir, ThrustDirection turnDir,
        Vec2 position, float rotation, Vec2 scale
) {

    // Create from record object
    public ThrusterProperties(Record record) {
        this(
                getThrustDirection(record.getString("moveDir")),
                getThrustDirection(record.getString("turnDir")),

                new Vec2(record.getFloat("posX"), record.getFloat("posY")),
                record.getFloat("rotation"),
                new Vec2(record.getFloat("scale"))
        );
    }

    private static ThrustDirection getThrustDirection(String value) {
        try {
            return ThrustDirection.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ThrustDirection.NONE;
        }
    }

}
