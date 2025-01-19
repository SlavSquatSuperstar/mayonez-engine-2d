package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.math.Random;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.EnemyFireController;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.movement.EnemyMovement;

import java.util.*;

/**
 * A computer-controlled enemy spaceship.
 *
 * @author SlavSquatSuperstar
 */
public class EnemySpaceship extends Spaceship {

    public EnemySpaceship(String name, Vec2 position) {
        super(name, position, SpaceshipPrefabs.SHUTTLE_PROPERTIES2);
    }

    @Override
    protected void init() {
        super.init();
        transform.setRotation(Random.randomAngle());

        // Movement
        Rigidbody rb;
        addComponent(rb = new Rigidbody(1f, 0.01f, 0.8f));
        rb.setVelocity(transform.getUp().mul(Random.randomFloat(2f, 15f)));
        addComponent(new EnemyMovement());

        // Weapons (Randomly Select Hardpoints)
        addComponent(new EnemyFireController(getHardpoints(properties.hardpoints())));
    }

    private static List<WeaponHardpoint> getHardpoints(List<WeaponHardpoint> hardpoints) {
        var rand = Random.randomInt(1, 10);
        if (rand < 6) return hardpoints.subList(0, 1);
        else if (rand < 9) return  hardpoints.subList(0, 2);
        else return hardpoints.subList(0, 3);
    }

}
