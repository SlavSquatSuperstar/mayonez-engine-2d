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
    private static final int ANIMATION_SPRITES = 8;
    private static final SpriteSheet EXPLOSION_TEXTURES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/combat/explosion.png",
            32, 32, ANIMATION_SPRITES, 0
    );

    // Factory Methods

    public static GameObject createPrefab(String name, Transform transform, float duration) {
        return new GameObject(name, transform, SpaceGameZIndex.EXPLOSION) {
            @Override
            protected void init() {
                addComponent(new Animator(EXPLOSION_TEXTURES,
                        duration / ANIMATION_SPRITES) {
                    @Override
                    public void onFinishAnimation() {
                        gameObject.destroy(); // destroy after finishing animation
                    }
                });
            }
        };
    }

}
