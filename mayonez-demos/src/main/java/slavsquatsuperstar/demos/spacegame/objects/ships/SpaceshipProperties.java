package slavsquatsuperstar.demos.spacegame.objects.ships;

/**
 * A set of properties for spaceships.
 *
 * @param spriteName      the filename of the spaceship's sprite
 * @param maxHealth       the spaceship's max health
 * @param maxShieldHealth the spaceship's max shield health
 */
public record SpaceshipProperties(
        String spriteName, float maxHealth, float maxShieldHealth
) {
}
