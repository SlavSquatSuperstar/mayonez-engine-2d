package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.movement.ThrustController;

/**
 * Plays a destruction sequence after a spaceship's health is depleted.
 *
 * @author SlavSquatSuperstar
 */
public class ShipDestruction extends Script {

    // Constants
    private static final float ANIMATION_DURATION = 1.6f;
    private static final int ANIMATION_SPRITES = 8;
    private static final SpriteSheet EXPLOSION_TEXTURES = Sprites.createSpriteSheet(
            "assets/textures/spacegame/explosion.png",
            32, 32, ANIMATION_SPRITES, 0
    );

    // Timer Components
    private boolean healthDepleted;
    private final Timer destructionTimer;

    // Component References
    private final Sprite mainSprite;
    private Animator explosionAnim;
    private Component fireProjectile, thrustController;

    public ShipDestruction(Sprite mainSprite) {
        destructionTimer = new Timer(ANIMATION_DURATION);
        this.mainSprite = mainSprite;
    }

    @Override
    public void init() {
        gameObject.addComponent(destructionTimer.setEnabled(false));
        explosionAnim = new Animator(EXPLOSION_TEXTURES,
                ANIMATION_DURATION / ANIMATION_SPRITES);
        gameObject.addComponent(explosionAnim);
    }

    @Override
    public void start() {
        healthDepleted = false;
        fireProjectile = gameObject.getComponent(FireProjectile.class);
        thrustController = gameObject.getComponent(ThrustController.class);
        explosionAnim.setEnabled(false);
    }

    @Override
    public void update(float dt) {
        if (!healthDepleted) super.update(dt);

        if (destructionTimer.isReady()) {
            gameObject.destroy();
        } else if (destructionTimer.getValue() <= ANIMATION_DURATION * 0.5f) {
            mainSprite.setEnabled(false);
        }
    }

    public void startDestructionSequence() {
        healthDepleted = true;
        destructionTimer.setEnabled(true);
        explosionAnim.setEnabled(true);

        if (fireProjectile != null) fireProjectile.setEnabled(false);
        if (thrustController != null) thrustController.setEnabled(false);
    }

}
