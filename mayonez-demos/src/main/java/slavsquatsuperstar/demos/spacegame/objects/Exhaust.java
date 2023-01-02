package slavsquatsuperstar.demos.spacegame.objects;

import mayonez.GameObject;
import mayonez.Transform;
import mayonez.graphics.sprites.SpriteSheet;
import mayonez.graphics.sprites.Animator;

/**
 * Exhaust plumes from a spaceship's engines.
 *
 * @author SlavSquatSuperstar
 */
public class Exhaust extends GameObject {

    private Transform parentXf, offsetXf;

    public Exhaust(String name, Transform parentXf, Transform offsetXf) {
        super(name);
        this.parentXf = parentXf;
        this.offsetXf = offsetXf;
    }

    @Override
    protected void init() {
        SpriteSheet sprites = SpriteSheet.create("assets/textures/spacegame/exhaust.png",
                16, 16, 3, 0);
        addComponent(new Animator(sprites, 0.25f));
    }

    @Override
    protected void onUserUpdate(float dt) {
        transform.set(parentXf.combine(offsetXf));
    }
}
