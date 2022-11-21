/**
 * The core library of MayonezEngine which contains all the base classes needed to create and run a game.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.base {
    // JVM
    requires java.base;
    requires transitive java.desktop;
    requires transitive kotlin.stdlib;

    // LWJGL
    requires org.lwjgl;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires org.lwjgl.stb;
    requires transitive org.joml;

    // Other Dependencies
    requires org.json;
    requires org.reflections;

    /* Public API */
    // Core
    exports mayonez;
    exports mayonez.graphics;
    exports mayonez.graphics.sprite;
    exports mayonez.input;
    exports mayonez.math;
    exports mayonez.util;
    // I/O
    exports mayonez.io;
    exports mayonez.io.image;
    exports mayonez.io.text;
    // Physics
    exports mayonez.physics;
    exports mayonez.physics.colliders;
    exports mayonez.physics.resolution;
    exports mayonez.physics.shapes;
    exports mayonez.scripts;
    // Other
    exports mayonez.annotations;
}