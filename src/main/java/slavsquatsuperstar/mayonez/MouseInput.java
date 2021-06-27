package slavsquatsuperstar.mayonez;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    // Mouse Fields
    private int mouseX, mouseY;
    private int dx, dy; // displacement of drag
    private boolean mousePressed, mouseDragged;
    private int button;
    private int clicks;

    // MouseListener Methods

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        button = e.getButton();
        clicks = e.getClickCount();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = mouseDragged = false;
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
        mouseDragged = true;
        dx = e.getX() - mouseX;
        dy = e.getY() - mouseY;
    }

    // Getters and Setters

//	public boolean buttonDown(String buttonName) {
//		for (MouseMapping b : MouseMapping.values())
//			if (b.toString().equalsIgnoreCase(buttonName))
//				return buttons[b.button() - 1];
//		return false;
//	}

    public int getX() {
        return mouseX;
    }

    public int getY() {
        return mouseY;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int button() {
        return button;
    }

    public int clicks() {
        return clicks;
    }

    public boolean dragged() {
        return mouseDragged;
    }

    public boolean pressed() {
        return mousePressed;
    }

    // Enum Declaration
//    enum MouseMapping {
//        LEFT_MOUSE(MouseEvent.BUTTON1), RIGHT_MOUSE(MouseEvent.BUTTON3);
//
//        private int button;
//
//        private MouseMapping(int button) {
//            this.button = button;
//        }
//
//        int button() {
//            return button;
//        }
//
//        @Override
//        public String toString() {
//            return name().toLowerCase();
//        }
//    }

}
