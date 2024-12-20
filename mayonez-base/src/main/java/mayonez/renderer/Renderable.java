package mayonez.renderer;

/**
 * An object that can be drawn to the screen through a {@link Renderer}.
 */
public interface Renderable {

    /**
     * The vertical layering of this object, or which order it should be drawn in.
     *
     * @return the z-index
     */
    int getZIndex();

    /**
     * Whether this object should be drawn this frame.
     *
     * @return if visible
     */
    boolean isEnabled();

    /**
     * Whether this object should be drawn in the user interface.
     *
     * @return if in UI
     */
    boolean isInUI();

}
