package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * A Goomba enemy that Mario can destroy.
 *
 * @author SlavSquatSuperstar
 */
class Goomba extends GameObject {

    private static final int[] SPRITE_SHEET_INDICES = {14, 17, 20, 23};
    private static final String[] GOOMBA_NAMES = {
            "Goomba", "Cool Goomba", "Vintage Goomba", "Vintage Cool Goomba"
    };

    private final int spriteIndex;

    Goomba(String name, int spriteIndex, Vec2 position) {
        super(name, new Transform(position, 0, new Vec2(1.5f)));
        this.spriteIndex = spriteIndex;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(MarioScene.CHARACTER_LAYER));
        addComponent(MarioScene.SPRITES.getSprite(spriteIndex));

        addComponent(new GoombaController());
        addComponent(new BoxCollider(new Vec2(0.8f, 1)));
        addComponent(new Rigidbody(1f, 0.5f, 0f).setFixedRotation(true));

        var sceneMin = MarioScene.SCENE_HALF_SIZE.mul(-1f).add(new Vec2(0, 4));
        addComponent(new KeepInScene(sceneMin, MarioScene.SCENE_HALF_SIZE, KeepInScene.Mode.STOP));
    }

    // Factory Methods

    protected static GameObject createRandomGoomba(int type, Vec2 spawnPosition) {
        return new Goomba(GOOMBA_NAMES[type], SPRITE_SHEET_INDICES[type], spawnPosition);
    }

}
