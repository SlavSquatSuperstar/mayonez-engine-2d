package mayonez.application;

import mayonez.event.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.input.events.*;
import mayonez.input.mouse.*;
import org.lwjgl.glfw.GLFW;

/**
 * Receives mouse input events from GLFW.
 * <p>
 * Source: <a href="https://www.glfw.org/docs/latest/input_guide.html#input_mouse">
 * GLFW Input Guide § Mouse Input</a>
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
class GLMouseManager implements MouseInputHandler {

    // Mouse Callbacks

    /**
     * The mouse button callback method for GLFW.
     *
     * @param window the window id
     * @param button the GLFW button code
     * @param action the event type
     * @param mods   any modifier keys
     */
    @SuppressWarnings("unused")
    void mouseButtonCallback(long window, int button, int action, int mods) {
        MouseButtonEvent event;
        switch (action) {
            case GLFW.GLFW_PRESS -> event = new MouseButtonEvent(button, true, GLFW.glfwGetTime());
            case GLFW.GLFW_RELEASE -> event = new MouseButtonEvent(button, false);
            // According to docs, GLFW_REPEAT never occurs with mouse
            default -> {
                return;
            }
        }
        getEventSystem().broadcast(event);
    }

    /**
     * The mouse position callback method for GLFW.
     *
     * @param window the window id
     * @param xPos   the x position of the cursor
     * @param yPos   the y position of the cursor
     */
    @SuppressWarnings("unused")
    void mousePosCallback(long window, double xPos, double yPos) {
        getEventSystem().broadcast(new MouseMoveEvent((float) xPos, (float) yPos));
    }

    /**
     * The mouse scroll callback method for GLFW.
     *
     * @param window  the window id
     * @param xOffset the x offset of the scroll wheel
     * @param yOffset the y offset of the scroll wheel
     */
    @SuppressWarnings("unused")
    void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getEventSystem().broadcast(new MouseScrollEvent((float) xOffset, (float) yOffset));
    }

    @Override
    public EventSystem<MouseInputEvent> getEventSystem() {
        return InputEvents.MOUSE_EVENTS;
    }

    @Override
    public int getButtonCode(Button button) {
        return button.getGlCode();
    }

}
