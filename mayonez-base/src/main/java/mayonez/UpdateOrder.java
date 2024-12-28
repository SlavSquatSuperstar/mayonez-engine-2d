package mayonez;

/**
 * The order components in a game object are updated every frame. Components with a lower
 * order are updated first. Setting the update order is useful if one component depends
 * on another component's information per frame. Order only affects components within
 * the same game object.
 */
public enum UpdateOrder {

    /**
     * A component that takes the player input, updated first.
     */
    INPUT(0),

    /**
     * A component that handles an object's movement and collision resolution,
     * updated after input.
     */
    PHYSICS(10),

    /**
     * A component that detects an object's collisions, updated after physics.
     */
    COLLISION(20),

    /**
     * A component that prepares an object for rendering, updated after collision.
     */
    PRE_RENDER(30),

    /**
     * A component that handles an object's rendering, updated after pre-render.
     */
    RENDER(40),

    /**
     * Any generic script not related to input, physics, or rendering, updated last.
     */
    SCRIPT(50);

    final int order;

    UpdateOrder(int order) {
        this.order = order;
    }

}
