package slavsquatsuperstar.demos.spacegame;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.mayonez.graphics.sprite.Sprite;
import slavsquatsuperstar.mayonez.input.KeyInput;
import slavsquatsuperstar.mayonez.math.Vec2;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.scripts.KeyMovement;
import slavsquatsuperstar.mayonez.scripts.MoveMode;
import slavsquatsuperstar.mayonez.util.Colors;

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
                        getRigidbody().applyAngularImpulse(0.25f * KeyInput.getAxis("horizontal2"));
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        Mayonez.setUseGL(false);
        Mayonez.setScene(new SpaceGameScene());
        Mayonez.start();
    }
}
