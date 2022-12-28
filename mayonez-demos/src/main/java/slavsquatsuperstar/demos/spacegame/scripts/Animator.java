package slavsquatsuperstar.demos.spacegame.scripts;

import mayonez.Script;
import mayonez.graphics.sprites.Sprite;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.scripts.Timer;

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
        for (Sprite spr : sprites) gameObject.addComponent(spr.setEnabled(false));
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
            hideFrame(currentFrame); // hide last farme
            currentFrame = (currentFrame + 1) % numFrames; // update frame count
            showFrame(currentFrame); // show new frame
            animTimer.reset();
        }
    }

    private void hideFrame(int frame) {
        sprites[frame].setEnabled(false);
    }

    private void showFrame(int frame) {
        sprites[frame].setEnabled(true);
    }

}
