package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.scripts.*;

/**
 * Animates a game object by swapping between different sprites.
 *
 * @author SlavSquatSuperstar
 */
public class Animator extends Script {

    private final Sprite[] sprites; // TODO swap textures instead
    private final int numFrames;
    private int currentFrame;
    private final Timer animTimer;

    public Animator(Sprite[] sprites, float animCooldown) {
        this.sprites = sprites;
        this.numFrames = sprites.length;
        currentFrame = 0;
        animTimer = new Timer(animCooldown);
    }

    public Animator(SpriteSheet sprites, float animCooldown) {
        this(sprites.toSpriteArray(), animCooldown);
    }

    @Override
    public void init() {
        for (var spr : sprites) gameObject.addComponent(spr.setEnabled(false));
        gameObject.addComponent(animTimer);
    }

    @Override
    public void start() {
        animTimer.start();
        showFrame(currentFrame);
    }

    @Override
    public void update(float dt) {
        if (animTimer.isReady()) {
            setFrame((currentFrame + 1) % numFrames); // update frame count
            animTimer.reset();
        }
    }

    // Frame Visibility Methods

    /**
     * Hide all frames and show the current one.
     *
     * @param frame the frame number to show
     */
    public void setFrame(int frame) {
        if (frame >= 0 && frame < numFrames) {
            hideFrame(currentFrame); // hide last frame
            currentFrame = frame;
            showFrame(currentFrame); // show next frame
        } else {
            hideFrame(currentFrame); // hide all frames
        }
    }

    @Override
    public void onEnable() {
        showFrame(currentFrame);
        animTimer.setEnabled(true);
    }

    @Override
    public void onDisable() {
        hideFrame(currentFrame);
        animTimer.setEnabled(false);
    }

    private void hideFrame(int frame) {
        sprites[frame].setEnabled(false);
    }

    private void showFrame(int frame) {
        sprites[frame].setEnabled(true);
    }

}
