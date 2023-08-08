package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.io.*;
import mayonez.io.text.*;
import mayonez.math.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.ZIndex;

import java.util.*;

/**
 * Creates prefab thruster objects for spaceships.
 *
 * @author SlavSquatSuperstar
 */
public final class ThrusterPrefabs {

    // Constants

    private static final SpriteSheet EXHAUST_TEXTURES = Sprites.createSpriteSheet(
            "assets/textures/spacegame/exhaust.png",
            16, 16, 4, 0);
    private static final CSVFile THRUSTER_DATA = Assets.getAsset(
            "assets/data/spacegame/thrusters.csv", CSVFile.class);

    private ThrusterPrefabs() {
    }

    // Factory Methods

    public static List<Thruster> addThrustersToObject(GameObject parent) {
        List<Thruster> thrusters = new ArrayList<>();
        if (THRUSTER_DATA == null) return thrusters;

        for (var record : THRUSTER_DATA.readCSV()) {
            Thruster thruster;
            try {
                thruster = getThruster(record);
                thrusters.add(thruster);
            } catch (IllegalArgumentException e) {
                continue;
            }

            var objName = "%s Thruster".formatted(record.getString("name"));
            addThrusterObject(parent, thruster, objName, getOffsetXf(record));
        }
        return thrusters;
    }

    private static Thruster getThruster(Record record) throws IllegalArgumentException {
        var moveDir = ThrustDirection.valueOf(record.getString("moveDir").toUpperCase());
        var turnDir = getTurnDir(record);

        if (turnDir == null) {
            return new Thruster(moveDir);
        } else {
            return new Thruster(moveDir, turnDir);
        }
    }

    private static ThrustDirection getTurnDir(Record record) throws IllegalArgumentException {
        var turnDirStr = record.getString("turnDir");
        if (turnDirStr.equals("none")) {
            return null;
        } else {
            return ThrustDirection.valueOf(turnDirStr.toUpperCase());
        }
    }

    private static Transform getOffsetXf(Record record) {
        var position = new Vec2(record.getFloat("posX"), record.getFloat("posY"));
        var rotation = record.getFloat("rotation");
        var scale = new Vec2(record.getFloat("scale"));
        return new Transform(position, rotation, scale);
    }

    private static void addThrusterObject(
            GameObject parent, Thruster thruster, String name, Transform offsetXf
    ) {
        parent.getScene().addObject(new GameObject(name) {
            @Override
            protected void init() {
                setZIndex(ZIndex.EXHAUST);
                addComponent(thruster);
                addComponent(new Animator(EXHAUST_TEXTURES, 0.15f));
                addComponent(new Script() {
                    @Override
                    public void debugRender() {
                        transform.set(parent.transform.combine(offsetXf));
                    }
                });
            }
        });
    }

}
