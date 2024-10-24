package mayonez.graphics;

/**
 * Which window and rendering framework a class is created for.
 *
 * @author SlavsSquatSuperstar
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
