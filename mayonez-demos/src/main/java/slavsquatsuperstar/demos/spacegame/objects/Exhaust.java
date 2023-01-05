package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.graphics.sprites.Animator;
import mayonez.graphics.sprites.SpriteSheet;

/**
 * Exhaust plumes from a spaceship's engines.
 *
 * @author SlavSquatSuperstar
 */
public class Exhaust extends GameObject {

    private static final SpriteSheet sprites = SpriteSheet.create("assets/textures/spacegame/exhaust.png",
            16, 16, 3, 0);
    private Transform parentXf, offsetXf; // todo move to game object

    public Exhaust(String name, Transform parentXf, Transform offsetXf) {
        super(name);
        this.parentXf = parentXf;
        this.offsetXf = offsetXf;
        setZIndex(ZIndex.EXHAUST.zIndex);
    }

    @Override
    protected void init() {
        addComponent(new Animator(sprites, 0.25f));
    }

    @Override
    protected void onUserUpdate(float dt) {
        transform.set(parentXf.combine(offsetXf));
    }

}
