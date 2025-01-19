package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.graphics.sprites.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

import java.util.*;

/**
 * Creates prefab thruster objects for spaceships.
 *
 * @author SlavSquatSuperstar
 */
public final class ThrusterPrefabs {

    // Assets
    private static final CSVFile THRUSTER_DATA;
    private static final List<ThrusterProperties> THRUSTER_PROPERTIES;
    private static final SpriteSheet EXHAUST_TEXTURES;

    static {
        THRUSTER_DATA = Assets.getAsset(
                "assets/spacegame/data/shuttle_thrusters.csv", CSVFile.class);
        if (THRUSTER_DATA == null) {
            THRUSTER_PROPERTIES = Collections.emptyList();
        } else {
            THRUSTER_PROPERTIES = THRUSTER_DATA.readCSV().stream()
                    .map(ThrusterProperties::new).toList();
        }
        EXHAUST_TEXTURES = Sprites.createSpriteSheet(
                "assets/spacegame/textures/ships/exhaust.png",
                16, 16, 4, 0);
    }

    private ThrusterPrefabs() {
    }

    // Factory Methods

    public static List<Thruster> addThrustersToObject(GameObject parent) {
        var thrusters = new ArrayList<Thruster>();
        for (var property : THRUSTER_PROPERTIES) {
            var thruster = new Thruster(property);
            thrusters.add(thruster);

            var offsetXf = new Transform(property.position(), property.rotation(), property.scale());
            addThrusterObject(parent, thruster, offsetXf);
        }
        return thrusters;
    }

    // TODO no more objects
    private static void addThrusterObject(
            GameObject parent, Thruster thruster, Transform offsetXf
    ) {
        parent.getScene().addObject(new GameObject("Thruster") {
            @Override
            protected void init() {
                setZIndex(SpaceGameZIndex.EXHAUST);
                addComponent(thruster);
                addComponent(new Animator(EXHAUST_TEXTURES, 0.15f));
                addComponent(new Script() {
                    @Override
                    protected void debugRender() {
                        transform.set(parent.transform.combine(offsetXf));
                    }
                });
            }
        });
    }

}
