package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.scripts.*;

/**
 * Animates a sprite by swapping between different textures.
 *
 * @author SlavSquatSuperstar
 */
public class Animator extends Script {

    // Animation Fields
    private final Texture[] textures;
    private final int numFrames;
    private int currentFrame;

    // Components
    private Sprite sprite;
    private final Timer animTimer;

    public Animator(Texture[] textures, float animCooldown) {
        this.textures = textures;
        this.numFrames = textures.length;
        currentFrame = 0;
        animTimer = new Timer(animCooldown);
    }

    public Animator(SpriteSheet sprites, float animCooldown) {
        this(sprites.getTextures(), animCooldown);
    }

    @Override
    public void init() {
        sprite = Sprites.createSprite(textures[0]);
        gameObject.addComponent(sprite.setEnabled(false));
        gameObject.addComponent(animTimer);
    }

    @Override
    public void start() {
        animTimer.reset();
        animTimer.start();
        setSpriteTexture(0);
    }

    @Override
    public void update(float dt) {
        if (animTimer.isReady()) {
            setFrame((currentFrame + 1) % numFrames); // update frame count
            animTimer.reset();
        }
    }

    // Set Frame Methods

    /**
     * Switches the animation to the given frame.
     *
     * @param frame the frame index to show
     */
    public void setFrame(int frame) {
        if (frame >= 0 && frame < numFrames) {
            currentFrame = frame;
            setSpriteTexture(currentFrame);
        } else {
            setSpriteVisible(false);
        }
    }

    private void setSpriteTexture(int frame) {
        sprite.setTexture(textures[frame]);
        setSpriteVisible(true);
    }

    /**
     * Sets the visibility of the sprite.
     *
     * @param visible if the sprite should be enabled
     */
    public void setSpriteVisible(boolean visible) {
        sprite.setEnabled(visible);
    }

    // Callback Methods

    @Override
    public void onEnable() {
        setSpriteVisible(true);
        animTimer.setEnabled(true);
    }

    @Override
    public void onDisable() {
        setSpriteVisible(false);
        animTimer.setEnabled(false);
    }

}
