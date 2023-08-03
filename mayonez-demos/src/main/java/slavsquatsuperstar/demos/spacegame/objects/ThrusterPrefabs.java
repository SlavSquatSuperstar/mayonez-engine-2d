package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.scripts.ThrustDirection;
import slavsquatsuperstar.demos.spacegame.scripts.Thruster;

/**
 * Creates prefab thrusts for spaceships.
 *
 * @author SlavSquatSuperstar
 */
public final class ThrusterPrefabs {

    private ThrusterPrefabs() {
    }

    public static Thruster[] addThrustersToObject(GameObject parent) {
        // Sub-Objects
        var lBack = new Thruster(ThrustDirection.FORWARD);
        var rBack = new Thruster(ThrustDirection.FORWARD);
        var lFront = new Thruster(ThrustDirection.BACKWARD);
        var rFront = new Thruster(ThrustDirection.BACKWARD);
        var fLeft = new Thruster(ThrustDirection.RIGHT, ThrustDirection.TURN_RIGHT);
        var bLeft = new Thruster(ThrustDirection.RIGHT, ThrustDirection.TURN_LEFT);
        var fRight = new Thruster(ThrustDirection.LEFT, ThrustDirection.TURN_LEFT);
        var bRight = new Thruster(ThrustDirection.LEFT, ThrustDirection.TURN_RIGHT);

        // Rear Thrusters
        addThrusterObject(parent, lBack, "Left Rear Thruster",
                new Vec2(-0.1f, -0.6f), 0f, new Vec2(0.3f));
        addThrusterObject(parent, rBack, "Right Rear Thruster",
                new Vec2(0.1f, -0.6f), 0f, new Vec2(0.3f));

        // Front Thrusters
        addThrusterObject(parent, lFront, "Left Front Thruster",
                new Vec2(-0.075f, 0.46f), 180f, new Vec2(0.1f));
        addThrusterObject(parent, rFront, "Right Front Thruster",
                new Vec2(0.075f, 0.46f), 180f, new Vec2(0.1f));

        // Left Thrusters
        addThrusterObject(parent, fLeft, "Front Left Thruster",
                new Vec2(-0.14f, 0.39f), -90, new Vec2(0.08f));
        addThrusterObject(parent, bLeft, "Rear Left Thruster",
                new Vec2(-0.2f, -0.36f), -90, new Vec2(0.08f));

        // Right Thrusters
        addThrusterObject(parent, fRight, "Front Right Thruster",
                new Vec2(0.14f, 0.39f), 90, new Vec2(0.08f));
        addThrusterObject(parent, bRight, "Rear Right Thruster",
                new Vec2(0.2f, -0.36f), 90, new Vec2(0.08f));

        return new Thruster[]{lBack, rBack, lFront, rFront, fLeft, bLeft, fRight, bRight};
    }

    private static void addThrusterObject(GameObject parent, Thruster thruster, String name, Vec2 position, float rotation, Vec2 scale) {
        parent.getScene().addObject(
                Thruster.createPrefab(thruster, name, parent,
                        new Transform(position, rotation, scale))
        );
    }
}
