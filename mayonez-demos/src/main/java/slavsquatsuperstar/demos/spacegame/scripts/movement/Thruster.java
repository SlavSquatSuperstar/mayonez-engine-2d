package slavsquatsuperstar.demos.spacegame.scripts.movement;

import mayonez.Script;
import mayonez.Transform;
import mayonez.graphics.sprites.Animator;
import slavsquatsuperstar.demos.spacegame.objects.Exhaust;

/**
 * A script representing a spaceship engine and assigns it a direction.
 *
 * @author SlavSquatSuperstar
 */
public class Thruster extends Script {

    public final ThrustDirection moveDir;
    public final ThrustDirection turnDir;
    private Animator exhaust;
    private boolean moveEnabled, turnEnabled;

    // Create a movement thruster
    public Thruster(ThrustDirection moveDir) {
        this(moveDir, ThrustDirection.NONE);
    }

    // Create a maneuvering thruster
    public Thruster(ThrustDirection moveDir, ThrustDirection turnDir) {
        this.moveDir = moveDir;
        this.turnDir = turnDir;
    }

    @Override
    public void start() {
        exhaust = gameObject.getComponent(Animator.class);
        System.out.println(exhaust);
        moveEnabled = false;
        turnEnabled = false;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (exhaust != null) exhaust.setEnabled(moveEnabled || turnEnabled);
    }

    public void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    public void setTurnEnabled(boolean turnEnabled) {
        this.turnEnabled = turnEnabled;
    }

    public static Exhaust createObject(Thruster thruster, String name, Transform parentXf, Transform offsetXf) {
        return new Exhaust(name, parentXf, offsetXf) {
            @Override
            protected void init() {
                super.init();
                addComponent(thruster);
            }
        };
    }
}
