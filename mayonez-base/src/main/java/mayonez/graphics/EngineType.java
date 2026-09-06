package mayonez.graphics;

/**
 * A graphics API for rendering a scene to a window. Some APIs may not be
 * feature-complete.
 *
 * @author SlavsSquatSuperstar
 */
public enum EngineType {
    /**
     * Java's built-in Abstract Window Toolkit and Swing packages.
     * Does not currently support UI rendering.
     */
    AWT,
    /**
     * LWJGL's low-level OpenGL library. Runs on top of GLFW.
     */
    GL
}
