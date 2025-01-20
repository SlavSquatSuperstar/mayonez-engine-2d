package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
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
    private static final SpriteSheet EXHAUST_TEXTURES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/ships/exhaust.png",
            16, 16, 4, 0);

    private ThrusterPrefabs() {
    }

    // Factory Methods

    static List<Thruster> getThrusters(List<ThrusterProperties> thrusterProperties) {
        return thrusterProperties.stream().map(Thruster::new).toList();
    }

    static List<GameObject> getThrusterObjects(List<Thruster> thrusters, Transform parentXf) {
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
