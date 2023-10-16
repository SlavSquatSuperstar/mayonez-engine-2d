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

    // Subprojects
    requires transitive mayonez.core;

    // Base API
    exports mayonez;
    exports mayonez.config;
    exports mayonez.event;
    exports mayonez.launcher;

    exports mayonez.input;
    exports mayonez.input.keyboard;
    exports mayonez.input.mouse;

    exports mayonez.assets;
    exports mayonez.assets.text;

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

    // Addons
    exports mayonez.scripts;
    exports mayonez.scripts.mouse;
    exports mayonez.scripts.movement;
}