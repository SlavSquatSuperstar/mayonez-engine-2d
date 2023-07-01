package mayonez.graphics;

/**
 * An object that can be drawn to the screen through a {@link mayonez.graphics.renderer.Renderer}.
 */
public interface Renderable {

    /**
     * Which order this object should be drawn in, or the vertical layering.
     *
     * @return the z-index
     */
    int getZIndex();

    /**
     * Whether this object should be drawn this frame.
     *
     * @return if enabled
     */
    boolean isEnabled();

}
