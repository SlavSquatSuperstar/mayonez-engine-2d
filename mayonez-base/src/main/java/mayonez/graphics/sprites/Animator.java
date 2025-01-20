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
     * Creates an animation from a sprite sheet that will loop through all the sprites.
     *
     * @param sprites         the frames of the animation
     * @param secondsPerFrame how much time to spend on each frame
     */
    public Animator(SpriteSheet sprites, float secondsPerFrame) {
        super(UpdateOrder.RENDER);
        this.textures = sprites.getTextures();
        this.numFrames = textures.length;
        currentFrame = 0;
        animTimer = new Timer(secondsPerFrame);
    }

    @Override
    protected void init() {
        sprite = Sprites.createSprite(textures[0]);
        gameObject.addComponent(sprite.setEnabled(false));
    }

    @Override
    protected void start() {
        animTimer.reset();
        setSpriteTexture(0);
    }

    @Override
    protected void update(float dt) {
        animTimer.countDown(dt);
        if (!animTimer.isPaused() && animTimer.isReady()) {
            // update frame count
            if (currentFrame == numFrames - 1) {
                currentFrame = 0;
                onFinishAnimation();
            } else {
                currentFrame += 1;
            }
            setFrame(currentFrame);
            animTimer.reset();
        }
    }

    // Animation Methods

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

    /**
     * Resumes or pauses the animation.
     *
     * @param enabled if the animation should play, true by default
     */
    public void setAnimationEnabled(boolean enabled) {
        animTimer.setPaused(!enabled);
    }

    // Sprite Methods

    /**
     * Sets the transform of this component's animated sprite.
     *
     * @param spriteXf the sprite transform
     */
    public void setSpriteTransform(Transform spriteXf) {
        this.sprite.setSpriteTransform(spriteXf);
    }

    /**
     * Sets the visibility of the sprite.
     *
     * @param visible if the sprite should be enabled
     */
    public void setSpriteVisible(boolean visible) {
        if (sprite == null) return; // not initialized yet
        sprite.setEnabled(visible);
    }

    private void setSpriteTexture(int frame) {
        if (sprite == null) return; // not initialized yet
        sprite.setTexture(textures[frame]);
        setSpriteVisible(true);
    }

    // Callback Methods

    @Override
    protected void onEnable() {
        setSpriteVisible(true);
        setAnimationEnabled(true);
    }

    @Override
    protected void onDisable() {
        setSpriteVisible(false);
        setAnimationEnabled(false);
    }

    /**
     * Custom user behavior for when this animation finishes looping once.
     */
    public void onFinishAnimation() {
    }

}
