package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.graphics.*;
import mayonez.math.*;

/**
 * A set of properties for asteroids.
 *
 * @param radius      the asteroid's size
 * @param color       the asteroid's color
 * @param spriteIndex the asteroid's sprite number
 */
public record AsteroidProperties(float radius, Color color, int spriteIndex) {

    public Vec2 getScale() {
        var width = AsteroidPrefabs.addRandomError(radius, 0.2f);
        var height = AsteroidPrefabs.addRandomError(radius, 0.2f);
        return new Vec2(width, height);
    }

    public AsteroidProperties setRadius(float radius) {
        return new AsteroidProperties(radius, this.color, this.spriteIndex);
    }

}
