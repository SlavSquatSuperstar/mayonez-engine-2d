/**
 * The foundational module of Mayonez Engine, which contains all the base classes needed
 * to run an application and develop a game.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.base {
    // JVM
    requires java.desktop;

    // LWJGL
    requires org.lwjgl;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires org.lwjgl.stb;

    // Other Dependencies
    requires kotlin.stdlib;
    requires org.joml;
    requires org.json;

    // Utilities
    exports mayonez.annotations;
    exports mayonez.util;

    // Base Application
    exports mayonez;
    exports mayonez.config;
    exports mayonez.event;
    exports mayonez.input;

    // File IO
    exports mayonez.io;
    exports mayonez.io.scanner;
    exports mayonez.io.text;

    // Assets
    exports mayonez.assets;
    exports mayonez.assets.image;
    exports mayonez.assets.text;

    // Math
    exports mayonez.math;
    exports mayonez.math.shapes;

    // Physics
    exports mayonez.physics;
    exports mayonez.physics.colliders;
    exports mayonez.physics.dynamics;

    // Graphics
    exports mayonez.graphics;
    exports mayonez.graphics.camera;
    exports mayonez.graphics.debug;
    exports mayonez.graphics.font;
    exports mayonez.graphics.sprites;
    exports mayonez.graphics.textures;
    exports mayonez.graphics.ui;

    // Renderer
    exports mayonez.renderer.batch;

    // Extras
    exports mayonez.scripts;
    exports mayonez.scripts.camera;
    exports mayonez.scripts.mouse;
    exports mayonez.scripts.movement;
}