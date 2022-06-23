/**
 * The core library of MayonezEngine which contains all the base classes needed to create and run a game.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.base {
    // JVM
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
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires org.reflections;

    // Public API
    exports slavsquatsuperstar.math;
    exports slavsquatsuperstar.math.geom;
    exports slavsquatsuperstar.mayonez;
    exports slavsquatsuperstar.mayonez.fileio;
    exports slavsquatsuperstar.mayonez.graphics;
    exports slavsquatsuperstar.mayonez.input;
    exports slavsquatsuperstar.mayonez.physics2d;
    exports slavsquatsuperstar.mayonez.physics2d.colliders;
    exports slavsquatsuperstar.mayonez.scripts;
}