package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.scripts.*;

/**
 * Animates a sprite by swapping between different textures provided by a
 * {@link SpriteSheet}. The animation can be toggled using {@link #setEnabled},
 * and the texture visibility can be set using {@link #setVisible}.
 *
 * @author SlavSquatSuperstar
 */
public class Animator extends Node {

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
        this.textures = sprites.getTextures();
        this.numFrames = textures.length;
        currentFrame = 0;
        animTimer = new Timer(secondsPerFrame);
    }

    // TODO extend sprite
    @Override
    protected void init() {
        sprite = Sprites.createSprite(textures[0]);
        sprite.setZIndex(getZIndex());
        addChild(sprite);
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
            setVisible(false);
        }
    }

    // Sprite Methods

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        // make sure sprite is initialized
        if (sprite != null) sprite.setVisible(visible);
    }

    private void setSpriteTexture(int frame) {
        if (sprite == null) return; // not initialized yet
        sprite.setTexture(textures[frame]);
        setVisible(true);
    }

    @Override
    public void setZIndex(int zIndex) {
        super.setZIndex(zIndex);
        if (sprite != null) sprite.setZIndex(zIndex);
    }

    // Callback Methods

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        animTimer.setPaused(!enabled);
    }

    /**
     * Custom user behavior for when this animation finishes looping once.
     */
    public void onFinishAnimation() {
    }

}
