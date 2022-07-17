package slavsquatsuperstar.mayonez.graphics;

/**
 * Which window/rendering framework a component is created for.
 */
public enum EngineType {
    /**
     * Java's default AWT and Swing packages.
     */
    AWT,
    /**
     * LWJGL's GLFW and OpenGL libraries.
     */
    GL
}
