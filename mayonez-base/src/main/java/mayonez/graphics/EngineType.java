package mayonez.graphics;

/**
 * Defines a backend (platform and/or renderer) for the game engine. The
 * platform manages the window and input, while the renderer refers to the
 * graphics API.
 * <p>
 * All backends share the same interface, but the implementation may vary.
 * Backends may also be portable or platform-specific.
 *
 * @author SlavsSquatSuperstar
 */
public enum EngineType {
    /**
     * Java's default AWT and Swing packages. Multiplatform, but does not
     * currently support UI rendering.
     */
    AWT,
    /**
     * LWJGL's GLFW and OpenGL libraries. Multiplatform, and supports all
     * features.
     */
    GL
}
