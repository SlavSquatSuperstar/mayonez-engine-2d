package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.Random;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * An animated explosion resulting from a spacecraft's destruction.
 *
 * @author SlavSquatSuperstar
 */
// TODO damage ships in radius
public class Explosion extends Animator {

    // Constants
    private static final int SHIP_EXPLOSION_SPRITES = 8;
    private static final SpriteSheet SHIP_EXPLOSION_SPRITESHEET;

    static {
        // Read sprite sheet
        SHIP_EXPLOSION_SPRITESHEET = Sprites.createSpriteSheet(
                "assets/spacegame/textures/combat/explosion.png",
                32, 32, SHIP_EXPLOSION_SPRITES, 0
        );
    }

    private final Node target;

    public Explosion(float duration, Node target) {
        super(SHIP_EXPLOSION_SPRITESHEET, duration / SHIP_EXPLOSION_SPRITES);
        this.target = target;
    }

    @Override
    protected void init() {
        super.init();
        var globalXf = target.getGlobalTransform();
        var transform = new Transform(globalXf.getPosition(), Random.randomAngle(), globalXf.getScale());
        setTransform(transform);
        setName("Explosion");
        setZIndex(SpaceGameZIndex.EXPLOSION);
    }

    @Override
    protected void update(float dt) {
        super.update(dt);
        // Follow the target until the target is destroyed
        if (!target.isDestroyed()) {
            setGlobalTransform(target.getGlobalTransform());
        }
    }

    @Override
    public void onFinishAnimation() {
        setDestroyed(); // Destroy after finishing animation
    }

}
