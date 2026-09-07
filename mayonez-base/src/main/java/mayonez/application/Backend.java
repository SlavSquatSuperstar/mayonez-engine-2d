package mayonez.application;

import mayonez.graphics.EngineType;

/**
 * A backend for the game engine that uses a windowing library and graphics
 * API, similar to a solution stack. All backends share the same interface, but
 * the implementation may vary, and some may lack certain features. Backends
 * may also use the same library for windowing and rendering. Mayonez Engine
 * currently supports two backends: GLFW/OpenGL and AWT. Both are portable,
 * but may have minor differences across platforms.
 * <p>
 * See also: <a href="https://github.com/ocornut/imgui/blob/master/docs/BACKENDS.md">ImGui Backends</a>
 *
 * @author SlavSquatSuperstar
 */
public record Backend(String name, WindowLibrary window, EngineType graphics) {

    @Override
    public String toString() {
        return name;
    }

}
