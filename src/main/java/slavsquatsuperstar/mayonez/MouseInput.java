package slavsquatsuperstar.mayonez;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    // Mouse Fields
    private int mouseX, mouseY;
    private int dx, dy; // drag displacement
    private boolean pressed, dragged;
    private int button;
    private int clicks;

    // MouseListener Methods

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
        button = e.getButton();
        clicks = e.getClickCount();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = dragged = false;
        dx = dy = 0;
        clicks = 0;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragged = true;
        dx = e.getX() - mouseX;
        dy = e.getY() - mouseY;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    // Getters and Setters

    public boolean buttonDown(String buttonName) {
        for (MouseMapping b : MouseMapping.values())
            if (b.toString().equalsIgnoreCase(buttonName))
                return pressed && button == b.button;
        return false;
    }

    public int getX() {
        return mouseX;
    }

    public int getY() {
        return mouseY;
    }

    // TODO world vs screen
    public Vector2 getPosition() {
        return new Vector2(mouseX, mouseY).div(Preferences.TILE_SIZE);
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public Vector2 getDisplacement() {
        return new Vector2(dx, dy).div(Preferences.TILE_SIZE);
    }

    public int button() {
        return button;
    }

    public int clicks() {
        return clicks;
    }

    public boolean dragged() {
        return dragged;
    }

    public boolean pressed() {
        return pressed;
    }

    // Enum Declaration
    enum MouseMapping {
        LEFT_MOUSE(MouseEvent.BUTTON1), RIGHT_MOUSE(MouseEvent.BUTTON3), MIDDLE_MOUSE(MouseEvent.BUTTON2);

        int button;

        MouseMapping(int button) {
            this.button = button;
        }

        @Override
        public String toString() {
            // ex: MouseMapping.LEFT_MOUSE returns "left mouse"
            return name().replace('_', ' ').toLowerCase();
        }
    }

}
