package slavsquatsuperstar.demos.spacegame.objects.asteroids;

import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * Creates prefab asteroids generated randomly or from destroyed asteroids.
 *
 * @author SlavSquatSuperstar
 */
public final class AsteroidPrefabs {

    // Constants
    static final float MIN_SMALL_RADIUS = 1f;
    static final float MIN_MEDIUM_RADIUS = 1.5f;
    static final float MIN_LARGE_RADIUS = 2.5f;
    static final float MAX_LARGE_RADIUS = 4f;

    // Assets
    // TODO medium asteroid textures
    private static final int NUM_LARGE_TEXTURES = 2;
    private static final Texture[] LARGE_ASTEROID_TEXTURES;
    private static final int NUM_MED_TEXTURES = 4;
    private static final SpriteSheet MED_ASTEROID_TEXTURES;
    private static final int NUM_SMALL_TEXTURES = 4;
    private static final SpriteSheet SMALL_ASTEROID_TEXTURES;

    static {
        // Read textures
        var ASTEROID_TEXTURE_FILES = new String[]{
                "assets/spacegame/textures/asteroids/asteroid1.png",
                "assets/spacegame/textures/asteroids/asteroid2.png"
        };
        LARGE_ASTEROID_TEXTURES = new Texture[NUM_LARGE_TEXTURES];
        for (int i = 0; i < NUM_LARGE_TEXTURES; i++) {
            LARGE_ASTEROID_TEXTURES[i] = Textures.getTexture(ASTEROID_TEXTURE_FILES[i]);
        }

        // Read spritesheets
        MED_ASTEROID_TEXTURES = Sprites.createSpriteSheet(
                "assets/spacegame/textures/asteroids/asteroids_medium.png",
                16, 16, NUM_MED_TEXTURES, 0);

        SMALL_ASTEROID_TEXTURES = Sprites.createSpriteSheet(
                "assets/spacegame/textures/asteroids/asteroids_small.png",
                8, 8, NUM_SMALL_TEXTURES, 0);
    }

    private AsteroidPrefabs() {
    }

    // Create Prefab Methods

    // Prefab Helper Methods

    public static AsteroidProperties getRandomProperties() {
        var radius = Random.randomFloat(MIN_LARGE_RADIUS, MAX_LARGE_RADIUS);
        var color = Color.grayscale(Random.randomInt(96, 176));
        var texture = getAsteroidTexture(radius);
        return new AsteroidProperties(radius, texture, color);
    }

    static Texture getAsteroidTexture(float radius) {
        Texture fragmentTexture;
        if (radius > MIN_LARGE_RADIUS) {
            fragmentTexture = getRandomLargeTexture();
        } else if (radius > MIN_MEDIUM_RADIUS) {
            fragmentTexture = getRandomMediumTexture();
        } else {
            fragmentTexture = getRandomSmallTexture();
        }
        return fragmentTexture;
    }

    private static Texture getRandomLargeTexture() {
        return LARGE_ASTEROID_TEXTURES[Random.randomInt(0, NUM_LARGE_TEXTURES - 1)];
    }

    private static Texture getRandomMediumTexture() {
        return MED_ASTEROID_TEXTURES.getTexture(Random.randomInt(0, NUM_MED_TEXTURES - 1));
    }

    private static Texture getRandomSmallTexture() {
        return SMALL_ASTEROID_TEXTURES.getTexture(Random.randomInt(0, NUM_SMALL_TEXTURES - 1));
    }

}
