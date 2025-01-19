package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.graphics.sprites.*;

/**
 * An individual spaceship engine with a thrust direction.
 *
 * @author SlavSquatSuperstar
 */
public class Thruster extends Script {

    private final ThrusterProperties properties;
    private boolean moveEnabled, turnEnabled;
    private Animator exhaustAnim;

    public Thruster(ThrusterProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void start() {
        moveEnabled = false;
        turnEnabled = false;
        exhaustAnim = gameObject.getComponent(Animator.class);
        exhaustAnim.setSpriteTransform(properties.offsetXf());
    }

    @Override
    protected void update(float dt) {
        exhaustAnim.setEnabled(moveEnabled || turnEnabled);
    }

    @Override
    protected void onDisable() {
        exhaustAnim.setEnabled(false);
    }

    // Getters and Setters

    ThrustDirection getMoveDir() {
        return properties.moveDir();
    }

    ThrustDirection getTurnDir() {
        return properties.turnDir();
    }

    void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    void setTurnEnabled(boolean turnEnabled) {
        this.turnEnabled = turnEnabled;
    }

}
