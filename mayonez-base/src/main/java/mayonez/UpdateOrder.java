package mayonez;

/**
 * The order components in a game object are updated every frame. Components with a lower
 * order are updated first. Setting the update order is useful if one component depends
 * on another component's information per frame. Order only affects components within
 * the same game object (to be changed). Users may also define the own update order constants.
 */
public record UpdateOrder(String name, int order) {

    /**
     * A component that takes the player input, updated first.
     */
    public static final UpdateOrder INPUT = new UpdateOrder("INPUT", 0);

    /**
     * A component that handles an object's movement and collision resolution,
     * updated after input.
     */
    public static final UpdateOrder PHYSICS = new UpdateOrder("PHYSICS", 10);

    /**
     * A component that detects an object's collisions, updated after physics.
     */
    public static final UpdateOrder COLLISION = new UpdateOrder("COLLISION", 20);

    /**
     * A component that prepares an object for rendering, updated after collision.
     */
    public static final UpdateOrder PRE_RENDER = new UpdateOrder("PRE-RENDER", 30);

    /**
     * A component that handles an object's rendering, updated after pre-render.
     */
    public static final UpdateOrder RENDER = new UpdateOrder("RENDER", 40);

    /**
     * Any generic script not related to input, physics, or rendering, updated last.
     */
    public static final UpdateOrder SCRIPT = new UpdateOrder("SCRIPT", 50);

    @Override
    public String toString() {
        return "%s (%d)".formatted(name.toUpperCase(), order);
    }

}
