package slavsquatsuperstar.demos.geometrydash;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.init.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.util.*;

public class GDLevel extends Scene {

    public GDLevel() {
        super("Level", (int) (Preferences.getScreenWidth() * 1.5f), (int) (Preferences.getScreenHeight() * 1f), 42);
        setBackground(Colors.LIGHT_GRAY);
    }

    @Override
    protected void init() {
        addObject(new GameObject("Ground", new Vec2(0, getHeight() * -0.5f)) {
            @Override
            protected void init() {
                addComponent(new Rigidbody(0f).setFixedRotation(true));
                addComponent(new BoxCollider(new Vec2(getWidth() + 2f, 2f)));
                addComponent(new ShapeSprite(Colors.BLACK, true));
            }
        });

        addObject(new GDPlayer("Player", new Vec2(0, -5)));
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher().setRunConfig(new RunConfig(false));
        launcher.startGame(new GDLevel());
    }

}
