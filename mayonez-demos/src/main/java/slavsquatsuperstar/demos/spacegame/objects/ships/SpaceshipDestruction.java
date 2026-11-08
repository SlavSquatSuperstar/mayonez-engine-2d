package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.*;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;
import slavsquatsuperstar.demos.spacegame.combat.Explosion;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.FireProjectile;
import slavsquatsuperstar.demos.spacegame.movement.SpaceshipMovement;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;

import java.util.*;

/**
 * Disables all ship systems and creates an explosion after a spaceship's
 * health is depleted.
 *
 * @author SlavSquatSuperstar
 */
public class SpaceshipDestruction extends TimerScript {

    // Constants
    private static final float EXPLOSION_DURATION = 0.8f;
    private static final float DESTRUCTION_DURATION = 0.6f;

    // References
    private final List<Node> shipSystems;

    public SpaceshipDestruction() {
        super(DESTRUCTION_DURATION);
        shipSystems = new ArrayList<>();
    }

    @Override
    protected void start() {
        shipSystems.clear();

        shipSystems.add(getSibling(FireProjectile.class));
        shipSystems.add(getSibling(ThrustController.class));
        shipSystems.add(getSibling(SpaceshipMovement.class));
        shipSystems.add(getSibling(KeepInScene.class));
    }

    @Override
    protected void update(float dt) {
        super.update(dt);
        if (this.isReady()) getParent().setDestroyed();
    }

    /**
     * Begin the ship destruction sequence.
     */
    public void startDestructionSequence() {
        if (this.isStarted()) return;
        this.setStarted(true);

        // Disable ship systems
        for (var system : shipSystems) {
            if (system != null) system.setEnabled(false);
        }
        shipSystems.clear();

        getScene().addNode(new Explosion(EXPLOSION_DURATION, this));
    }

}
