package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.graphics.sprites.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Creates animated explosion objects.
 *
 * @author SlavSquatSuperstar
 */
// TODO damage ships in radius
public class ExplosionPrefabs {

    // Constants
    private static final int SHIP_EXPLOSION_SPRITES = 8;
    private static final int ASTEROID_EXPLOSION_SPRITES = 4;
    private static final SpriteSheet SHIP_EXPLOSION_SPRITESHEET;
    private static final SpriteSheet ASTEROID_EXPLOSION_SPRITESHEET;

    static {
        // Read sprite sheets
        SHIP_EXPLOSION_SPRITESHEET = Sprites.createSpriteSheet(
                "assets/spacegame/textures/combat/explosion.png",
                32, 32, SHIP_EXPLOSION_SPRITES, 0
        );
        ASTEROID_EXPLOSION_SPRITESHEET = Sprites.createSpriteSheet(
                "assets/spacegame/textures/asteroids/asteroid_explosion.png",
                32, 32, ASTEROID_EXPLOSION_SPRITES, 0
        );
    }

    // Factory Methods

    public static GameObject createShipExplosionPrefab(
            String name, Transform transform, float duration
    ) {
        return new GameObject(name, transform, SpaceGameZIndex.EXPLOSION) {
            @Override
            protected void init() {
                addComponent(new Animator(SHIP_EXPLOSION_SPRITESHEET,
                        duration / SHIP_EXPLOSION_SPRITES) {
                    @Override
                    public void onFinishAnimation() {
                        gameObject.destroy(); // destroy after finishing animation
                    }
                });
            }
        };
    }

    public static GameObject createAsteroidExplosionPrefab(
            String name, Transform transform, float duration
    ) {
        return new GameObject(name, transform, SpaceGameZIndex.EXPLOSION) {
            @Override
            protected void init() {
                addComponent(new Animator(ASTEROID_EXPLOSION_SPRITESHEET,
                        duration / ASTEROID_EXPLOSION_SPRITES) {
                    @Override
                    public void onFinishAnimation() {
                        gameObject.destroy(); // destroy after finishing animation
                    }
                });
            }
        };
    }

}
