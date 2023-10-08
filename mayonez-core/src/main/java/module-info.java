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

    // Public API
    exports mayonez.annotations;
    exports mayonez.config;
    exports mayonez.event;
    exports mayonez.input;
    exports mayonez.input.keyboard;
    exports mayonez.input.mouse;
    exports mayonez.io;
    exports mayonez.io.image;
    exports mayonez.io.text;
    exports mayonez.math;
    exports mayonez.math.shapes;
    exports mayonez.util;
}