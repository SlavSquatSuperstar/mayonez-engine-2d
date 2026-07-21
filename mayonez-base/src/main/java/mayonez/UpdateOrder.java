package mayonez;

/**
 * The order components in a game object are updated every frame. Components with a lower
 * order are updated first. Setting the update order is useful if one component depends
 * on another component's information per frame. Order only affects components within
 * the same game object (to be changed). Users may also define their own update order constants.
 */
public final class UpdateOrder {

    private UpdateOrder() {
    }

    /**
     * A component that takes the player input, updated first.
     */
    public static final int INPUT = 0;

    /**
     * A component that manually controls an object's movement, updated after input.
     */
    public static final int MOVEMENT = 10;

    /**
     * Any generic script not related to input or movement, updated last.
     */
    public static final int SCRIPT = 20;

}
