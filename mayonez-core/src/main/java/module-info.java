/**
 * The foundational module of Mayonez Engine, which contains all the classes needed to
 * set up and run an application.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.core {
    // JVM
    requires java.desktop;
    requires kotlin.stdlib;

    // LWJGL
    requires org.lwjgl.glfw;

    // Other Dependencies
    requires org.json;

    // Shared API
    exports mayonez.annotations;
    exports mayonez.math;
    exports mayonez.math.shapes;
    exports mayonez.util;
}