package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * A set of properties for asteroids.
 *
 * @param radius  the asteroid's size
 * @param texture the asteroid's texture
 * @param color   the asteroid's color
 */
// TODO store area instead of radius
public record AsteroidProperties(float radius, Texture texture, Color color) {

    public float getHealth() {
        return Math.round(radius * radius);
    }

    public Vec2 getScale() {
        var width = getRandomError(radius, 0.2f);
        var height = getRandomError(radius, 0.2f);
        return new Vec2(width, height);
    }

    public static float getRandomError(float value, float percentError) {
        return value + Random.randomFloat(-value * percentError, value * percentError);
    }

}
