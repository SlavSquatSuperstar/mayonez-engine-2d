package mayonez;

/**
 * The order components in a game object are updated every frame. Components with a lower
 * order are updated first. Setting the update order is useful if one component depends
 * on another component's information per frame. Order only affects components within
 * the same game object (to be changed). Users may also define their own update order constants.
 */
public record UpdateOrder(String name, int order) {

    /**
     * A component that takes the player input, updated first.
     */
    public static final UpdateOrder INPUT = new UpdateOrder("INPUT", 0);

    /**
     * A component that manually controls an object's movement, updated after input.
     */
    public static final UpdateOrder MOVEMENT = new UpdateOrder("MOVEMENT", 20);

    /**
     * Any generic script not related to input or movement, updated last.
     */
    public static final UpdateOrder SCRIPT = new UpdateOrder("SCRIPT", 50);

    @Override
    public String toString() {
        return "%s (%d)".formatted(name.toUpperCase(), order);
    }

}
