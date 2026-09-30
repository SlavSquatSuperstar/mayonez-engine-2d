package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.graphics.sprites.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * An individual spaceship engine with a thrust direction.
 *
 * @author SlavSquatSuperstar
 */
public class Thruster extends Node {

    // Assets
    private static final SpriteSheet EXHAUST_TEXTURES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/ships/exhaust.png",
            16, 16, 4, 0);

    // Fields
    private final ThrusterProperties properties;
    private boolean moveEnabled, turnEnabled;
    private Animator exhaustAnim;

    public Thruster(ThrusterProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void init() {
        setTransform(properties.offsetXf());
        setZIndex(SpaceGameZIndex.EXHAUST);

        moveEnabled = false;
        turnEnabled = false;

        exhaustAnim = new Animator(EXHAUST_TEXTURES, 0.15f);
        addChild(exhaustAnim);
    }

    @Override
    protected void update(float dt) {
        exhaustAnim.setEnabled(moveEnabled || turnEnabled);
        exhaustAnim.setVisible(moveEnabled || turnEnabled);
    }

    @Override
    protected void onDisable() {
        exhaustAnim.setEnabled(false);
        exhaustAnim.setVisible(false);
    }

    // Getters and Setters

    String getDescription() {
        return properties.name();
    }

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
