/**
 * The core library of MayonezEngine, which contains all the base classes needed to
 * create and run a game.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.base {
    // JVM
    requires java.desktop;
    requires kotlin.stdlib;

    // LWJGL
    requires org.lwjgl;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires org.lwjgl.stb;
    requires org.joml;

    // Other Dependencies
    requires org.json;

    // Public API
    exports mayonez;
    exports mayonez.annotations;
    exports mayonez.config;
    exports mayonez.graphics;
    exports mayonez.graphics.camera;
    exports mayonez.graphics.debug;
    exports mayonez.graphics.sprites;
    exports mayonez.graphics.textures;
    exports mayonez.init;
    exports mayonez.io;
    exports mayonez.io.image;
    exports mayonez.io.text;
    exports mayonez.input;
    exports mayonez.math;
    exports mayonez.math.shapes;
    exports mayonez.physics;
    exports mayonez.physics.colliders;
    exports mayonez.physics.dynamics;
    exports mayonez.scripts;
    exports mayonez.scripts.mouse;
    exports mayonez.scripts.movement;
    exports mayonez.util;
}