package mayonez;

/**
 * The order components in a game object are updated every frame. Components with a lower
 * order are updated first. Setting the update order is useful if one component depends on
 * another component's information per frame.
 */
public enum UpdateOrder {

    /**
     * A script that takes the player input.
     */
    INPUT(0),

    /**
     * A component that handles an object's movement.
     */
    PHYSICS(10),

    /**
     * A component that handles an object's collisions.
     */
    COLLISION(20),

    /**
     * A component that handles an object's appearance.
     */
    RENDER(30),

    /**
     * Any generic script not related to input, motion, or rendering.
     */
    SCRIPT(40);

    final int order;

    UpdateOrder(int order) {
        this.order = order;
    }

}
