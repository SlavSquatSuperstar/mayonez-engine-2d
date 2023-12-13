/**
 * The foundational module of Mayonez Engine, which contains all the base classes needed
 * to run an application and develop a game.
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
    requires transitive mayonez.tools;

    // Base Application
    exports mayonez;
    exports mayonez.config;
    exports mayonez.event;
    exports mayonez.input;
    exports mayonez.launcher;

    // Assets
    exports mayonez.assets;
    exports mayonez.assets.text;

    // Renderer
    exports mayonez.graphics;
    exports mayonez.graphics.camera;
    exports mayonez.graphics.debug;
    exports mayonez.graphics.sprites;
    exports mayonez.graphics.textures;
    exports mayonez.graphics.ui;

    // Physics
    exports mayonez.physics;
    exports mayonez.physics.colliders;
    exports mayonez.physics.dynamics;

    // Addons
    exports mayonez.scripts;
    exports mayonez.scripts.mouse;
    exports mayonez.scripts.movement;
}