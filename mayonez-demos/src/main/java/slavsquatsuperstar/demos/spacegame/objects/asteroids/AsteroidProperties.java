package slavsquatsuperstar.demos.spacegame.objects.asteroids;

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
        var width = getRandomError(radius, 0.2f);
        var height = getRandomError(radius, 0.2f);
        return new Vec2(width, height);
    }

    public AsteroidProperties copyWithRadius(float radius) {
        return new AsteroidProperties(radius, this.color, this.spriteIndex);
    }

    public static float getRandomError(float value, float percentError) {
        return value + Random.randomFloat(-value * percentError, value * percentError);
    }

}
