package mayonez.application;

import mayonez.*;
import mayonez.event.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.input.events.*;

import java.awt.event.*;

/**
 * Receives mouse input events from AWT.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
class JMouseManager extends MouseAdapter implements MouseInputHandler {

    // Mouse Button Callbacks

    @Override
    public void mousePressed(MouseEvent e) {
        var time = Time.getTotalProgramSeconds();
        getEventSystem().broadcast(new MouseButtonEvent(e.getButton(), true, time));
        // Not relying on MouseEvent.clickCount since want to be similar to GL input
        // Can query double click interval with AWT, but not with GLFW
        // Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval")
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        getEventSystem().broadcast(new MouseButtonEvent(e.getButton(), false));
    }

    // Mouse Movement Callbacks

    @Override
    public void mouseMoved(MouseEvent e) {
        getEventSystem().broadcast(new MouseMoveEvent(e.getX(), e.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        getEventSystem().broadcast(new MouseMoveEvent(e.getX(), e.getY()));
    }

    // Mouse Scroll Callbacks

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // AWT only supports one direction of scroll
        getEventSystem().broadcast(new MouseScrollEvent(0f, e.getWheelRotation()));
    }

    // Input Handler Overrides

    @Override
    public EventSystem<MouseInputEvent> getEventSystem() {
        return InputEvents.MOUSE_EVENTS;
    }

    @Override
    public int getButtonCode(Button button) {
        return button.getAwtCode();
    }

}
