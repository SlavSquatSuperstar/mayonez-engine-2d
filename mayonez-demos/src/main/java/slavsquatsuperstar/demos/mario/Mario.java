package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * A controllable Mario character.
 *
 * @author SlavSquatSuperstar
 */
class Mario extends GameObject {

    Mario(Vec2 position) {
        super("Mario", new Transform(position, 0f, new Vec2(2f)), 1);
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(MarioScene.CHARACTER_LAYER));
        getScene().getCamera().setSubject(this);
        addComponent(new MarioController());

        addComponent(MarioScene.SPRITES.getSprite(0));
        addComponent(new BoxCollider(new Vec2(0.8f, 1)));
        addComponent(new Rigidbody(1f, 0.1f, 0f).setFixedRotation(true));

        var sceneMin = MarioScene.SCENE_HALF_SIZE.mul(-1f).add(new Vec2(0, 4));
        addComponent(new KeepInScene(sceneMin, MarioScene.SCENE_HALF_SIZE, KeepInScene.Mode.STOP));
    }

}
