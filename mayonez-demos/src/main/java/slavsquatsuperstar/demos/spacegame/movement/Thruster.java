package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.graphics.sprites.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * An individual spaceship engine with a thrust direction.
 *
 * @author SlavSquatSuperstar
 */
class Thruster extends Animator {

    // Assets
    private static final SpriteSheet EXHAUST_TEXTURES = Sprites.createSpriteSheet(
            "assets/spacegame/textures/ships/exhaust.png",
            16, 16, 4, 0);

    // Fields
    private final ThrusterProperties properties;
    private boolean moveEnabled, turnEnabled;

    Thruster(ThrusterProperties properties) {
        super(EXHAUST_TEXTURES, 0.15f);
        this.properties = properties;
    }

    @Override
    protected void init() {
        super.init();
        setName(properties.name() + " Thruster");
        setTransform(properties.offsetXf());
        setZIndex(SpaceGameZIndex.EXHAUST);

        moveEnabled = false;
        turnEnabled = false;
    }

    @Override
    protected void onEnable(boolean enabled) {
        /*
         * Can't set thruster active inside update
         * since disabling will prevent the check
         */
        super.onEnable(enabled);
        setVisible(enabled);
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
        setEnabled(moveEnabled || turnEnabled);
    }

    void setTurnEnabled(boolean turnEnabled) {
        this.turnEnabled = turnEnabled;
        setEnabled(moveEnabled || turnEnabled);
    }

}
