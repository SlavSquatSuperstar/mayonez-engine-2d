package slavsquatsuperstar.mayonez;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO world vs screen
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

    // Mouse Pointer Methods

    public float getX() {
        return (float) mouseX / Game.currentScene().getCellSize();
    }

    public float getY() {
        return (float) mouseY / Game.currentScene().getCellSize();
    }

    public int getXScreen() {
        return mouseX;
    }

    public int getYScreen() {
        return mouseY;
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public float getDx() {
        return (float) dx / Game.currentScene().getCellSize();
    }

    public int getDxScreen() {
        return dx;
    }

    public float getDy() {
        return (float) dy / Game.currentScene().getCellSize();
    }

    public int getDyScreen() {
        return dy;
    }

    public Vector2 getDisplacement() {
        return new Vector2(getDx(), getDy());
    }

    // Mouse State Methods

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
