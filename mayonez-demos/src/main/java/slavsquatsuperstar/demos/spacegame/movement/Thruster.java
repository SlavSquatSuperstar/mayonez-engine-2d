package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.graphics.sprites.*;
import slavsquatsuperstar.demos.spacegame.ZIndex;

/**
 * A script representing a spaceship engine and assigns it a direction.
 *
 * @author SlavSquatSuperstar
 */
public class Thruster extends Script {

    private static final SpriteSheet EXHAUST_TEXTURES = Sprites.createSpriteSheet(
            "assets/textures/spacegame/exhaust.png",
            16, 16, 4, 0);

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
    public void start() {
        moveEnabled = false;
        turnEnabled = false;
        exhaustAnim = gameObject.getComponent(Animator.class);
        if (exhaustAnim == null) setEnabled(false);
    }

    @Override
    public void update(float dt) {
        exhaustAnim.setEnabled(moveEnabled || turnEnabled);
    }

    public void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    public void setTurnEnabled(boolean turnEnabled) {
        this.turnEnabled = turnEnabled;
    }

    @Override
    public void onDisable() {
        exhaustAnim.setEnabled(false);
    }

    // Factory Method

    /**
     * Create a prefab representing exhaust plumes from a spaceship's engines.
     */
    public static GameObject createPrefab(Thruster thruster, String name, GameObject parentObj, Transform offsetXf) {
        return new GameObject(name) {
            @Override
            protected void init() {
                setZIndex(ZIndex.EXHAUST);
                addComponent(thruster);
                addComponent(new Animator(EXHAUST_TEXTURES, 0.15f));
                addComponent(new Script() {
                    @Override
                    public void debugRender() {
                        transform.set(parentObj.transform.combine(offsetXf));
                    }
                });
            }
        };
    }

}
