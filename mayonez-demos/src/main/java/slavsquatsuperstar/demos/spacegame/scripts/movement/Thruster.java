package slavsquatsuperstar.demos.spacegame.scripts.movement;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.Transform;
import mayonez.graphics.sprites.Animator;
import mayonez.graphics.sprites.SpriteSheet;
import slavsquatsuperstar.demos.spacegame.objects.ZIndex;

/**
 * A script representing a spaceship engine and assigns it a direction.
 *
 * @author SlavSquatSuperstar
 */
public class Thruster extends Script {

    private static final SpriteSheet sprites = SpriteSheet.create("assets/textures/spacegame/exhaust.png",
            16, 16, 3, 0);

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
        moveEnabled = false;
        turnEnabled = false;
    }

    @Override
    public void update(float dt) {
        if (exhaust != null) exhaust.setEnabled(moveEnabled || turnEnabled);
    }

    public void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    public void setTurnEnabled(boolean turnEnabled) {
        this.turnEnabled = turnEnabled;
    }

    // Factory Method

    /**
     * Create a prefab representing exhaust plumes from a spaceship's engines.
     */
    public static GameObject createObject(Thruster thruster, String name, GameObject parentObj, Transform offsetXf) {
        return new GameObject(name) {
            @Override
            protected void init() {
                setZIndex(ZIndex.EXHAUST.zIndex);
//                System.out.println(getParent());
                addComponent(thruster);
                addComponent(new Animator(sprites, 0.25f));
            }

            @Override
            protected void onUserUpdate(float dt) {
                transform.set(parentObj.transform.combine(offsetXf));
            }
        };
    }
}
