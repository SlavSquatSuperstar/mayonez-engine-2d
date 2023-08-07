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

    /**
     * Creates an animation from a spritesheet that will loop through all the sprites.
     *
     * @param sprites         the frames of the animation
     * @param secondsPerFrame how much time to spend on each frame
     */
    public Animator(SpriteSheet sprites, float secondsPerFrame) {
        this.textures = sprites.getTextures();
        this.numFrames = textures.length;
        currentFrame = 0;
        animTimer = new Timer(secondsPerFrame);
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
        setSpriteTexture(0);
    }

    @Override
    public void update(float dt) {
        if (animTimer.isEnabled() && animTimer.isReady()) {
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

    /**
     * Resumes or pauses the animation.
     *
     * @param enabled if the animation should play, true by default
     */
    public void setAnimationEnabled(boolean enabled) {
        animTimer.setEnabled(enabled);
    }

    // Callback Methods

    @Override
    public void onEnable() {
        setSpriteVisible(true);
        setAnimationEnabled(true);
    }

    @Override
    public void onDisable() {
        setSpriteVisible(false);
        setAnimationEnabled(false);
    }

}
