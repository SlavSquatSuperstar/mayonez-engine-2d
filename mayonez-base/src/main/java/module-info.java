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

    // Public API
    exports slavsquatsuperstar.util;
    exports slavsquatsuperstar.math;
    exports slavsquatsuperstar.mayonez;
    exports slavsquatsuperstar.mayonez.annotations;
    exports slavsquatsuperstar.mayonez.io;
    exports slavsquatsuperstar.mayonez.graphics;
    exports slavsquatsuperstar.mayonez.graphics.sprites;
    exports slavsquatsuperstar.mayonez.input;
    exports slavsquatsuperstar.mayonez.physics;
    exports slavsquatsuperstar.mayonez.physics.colliders;
    exports slavsquatsuperstar.mayonez.physics.collision;
    exports slavsquatsuperstar.mayonez.physics.shapes;
    exports slavsquatsuperstar.mayonez.scripts;
}