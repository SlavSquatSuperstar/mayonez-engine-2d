package slavsquatsuperstar.demos.spacegame.objects.ships;

import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterProperties;

import java.util.*;

/**
 * A set of properties for spaceships.
 *
 * @param spriteName      the filename of the spaceship's sprite
 * @param maxHealth       the spaceship's max health
 * @param maxShieldHealth the spaceship's max shield health
 * @param thrusters       the spaceship's thruster configuration
 * @param hardpoints      the spaceship's hardpoint configuration
 * @author SlavSquatSuperstar
 */
public record SpaceshipProperties(
        String spriteName, float maxHealth, float maxShieldHealth,
        List<ThrusterProperties> thrusters, List<WeaponHardpoint> hardpoints
) {
}
