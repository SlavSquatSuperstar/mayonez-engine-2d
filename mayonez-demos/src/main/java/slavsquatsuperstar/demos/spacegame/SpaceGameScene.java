package slavsquatsuperstar.demos.spacegame;

import mayonez.*;
import mayonez.graphics.sprite.Sprite;
import mayonez.input.KeyInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.scripts.KeyMovement;
import mayonez.scripts.MoveMode;
import mayonez.graphics.Colors;

public class SpaceGameScene extends Scene {

    public SpaceGameScene() {
        super("Space Game", Preferences.getScreenWidth() * 4, Preferences.getScreenHeight() * 4, 32f);
        setBackground(Colors.JET_BLACK);
        setGravity(new Vec2());
    }

    @Override
    protected void init() {
        addObject(new GameObject("Spaceship", Transform.scaleInstance(new Vec2(2, 2))) {
            @Override
            protected void init() {
//                getCamera().setSubject(this);
                addComponent(Sprite.create("assets/textures/spacegame/spaceship1.png"));
                addComponent(new Rigidbody(10f));
                addComponent(new KeyMovement(MoveMode.IMPULSE, 1f));
                addComponent(new Script() {
                    @Override
                    public void update(float dt) {
                        getRigidbody().applyAngularImpulse(-0.25f * KeyInput.getAxis("horizontal2"));
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.start(new SpaceGameScene());
    }
}
