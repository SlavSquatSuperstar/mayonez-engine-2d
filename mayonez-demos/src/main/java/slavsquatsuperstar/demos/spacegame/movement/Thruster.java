package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.graphics.sprites.*;

/**
 * An individual engine on a spaceship with a thrust direction.
 *
 * @author SlavSquatSuperstar
 */
public class Thruster extends Script {

    private Animator exhaustAnim;
    final ThrustDirection moveDir, turnDir;
    private boolean moveEnabled, turnEnabled;

    /**
     * Create a movement thruster for straight movement.
     *
     * @param moveDir the movement direction
     */
    public Thruster(ThrustDirection moveDir) {
        this(moveDir, ThrustDirection.NONE);
    }

    /**
     * Create a maneuvering thruster for straight movement and turning.
     *
     * @param moveDir the movement direction
     * @param turnDir the turning direction
     */
    public Thruster(ThrustDirection moveDir, ThrustDirection turnDir) {
        this.moveDir = moveDir;
        this.turnDir = turnDir;
    }

    @Override
    protected void start() {
        moveEnabled = false;
        turnEnabled = false;
        exhaustAnim = gameObject.getComponent(Animator.class);
        if (exhaustAnim == null) setEnabled(false);
    }

    @Override
    protected void update(float dt) {
        exhaustAnim.setEnabled(moveEnabled || turnEnabled);
    }

    public void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    public void setTurnEnabled(boolean turnEnabled) {
        this.turnEnabled = turnEnabled;
    }

    @Override
    protected void onDisable() {
        exhaustAnim.setEnabled(false);
    }

}
