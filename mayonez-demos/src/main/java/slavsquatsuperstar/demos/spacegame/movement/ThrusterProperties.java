package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.util.Record;

/**
 * A set of properties for spaceship thrusters.
 *
 * @author SlavSquatSuperstar
 */
public record ThrusterProperties(
        String name, ThrustDirection moveDir, ThrustDirection turnDir, Transform offsetXf
) {

    // Create from record object
    public ThrusterProperties(Record record) {
        this(record.getString("name"),
                getThrustDirection(record.getString("moveDir")),
                getThrustDirection(record.getString("turnDir")),
                getOffsetXf(record));
    }

    private static ThrustDirection getThrustDirection(String value) {
        try {
            return ThrustDirection.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ThrustDirection.NONE;
        }
    }

    private static Transform getOffsetXf(Record record) {
        return new Transform(
                new Vec2(record.getFloat("posX"), record.getFloat("posY")),
                record.getFloat("rotation"),
                new Vec2(record.getFloat("scale"))
        );
    }

}
