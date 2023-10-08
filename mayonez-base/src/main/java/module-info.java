/**
 * The core library of Mayonez Engine, which contains all the base classes for developing
 * a game.
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

    // Other Dependencies
    requires org.joml;
    requires org.json;

    // Subprojects
    requires transitive mayonez.core;

    // Public API

    // Base Module
    exports mayonez;
    exports mayonez.config;
    exports mayonez.input;
    exports mayonez.io;
    exports mayonez.io.image;
    exports mayonez.io.text;
    exports mayonez.launcher;

    // Renderer
    exports mayonez.graphics;
    exports mayonez.graphics.camera;
    exports mayonez.graphics.debug;
    exports mayonez.graphics.sprites;
    exports mayonez.graphics.textures;

    // Physics
    exports mayonez.physics;
    exports mayonez.physics.colliders;
    exports mayonez.physics.dynamics;

    // API
    exports mayonez.scripts;
    exports mayonez.scripts.mouse;
    exports mayonez.scripts.movement;
}