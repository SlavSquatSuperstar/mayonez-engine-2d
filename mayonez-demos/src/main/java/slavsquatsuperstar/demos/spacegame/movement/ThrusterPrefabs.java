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
    public static final List<ThrusterProperties> THRUSTER_PROPERTIES;
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

    public static List<Thruster> getThrusters(List<ThrusterProperties> thrusterProperties) {
        return thrusterProperties.stream().map(Thruster::new).toList();
    }

    public static List<GameObject> getThrusterObjects(List<Thruster> thrusters, Transform parentXf) {
        return thrusters.stream()
                .map(thruster -> getThrusterObject(thruster, parentXf))
                .toList();
    }

    private static GameObject getThrusterObject(Thruster thruster, Transform parentXf) {
        return new GameObject("%s Thruster".formatted(thruster.getName())) {
            @Override
            protected void init() {
                setZIndex(SpaceGameZIndex.EXHAUST);
                addComponent(thruster);
                addComponent(new Animator(EXHAUST_TEXTURES, 0.15f) {
                    @Override
                    protected void debugRender() {
                        transform.set(parentXf);
                    }
                });
            }
        };
    }

}
