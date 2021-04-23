package mayonez;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

	// Singleton Field
	private static MouseInput instance;

	// Mouse Fields
	private static int mouseX, mouseY;
	private static boolean dragging;
	private static boolean[] buttons = new boolean[MouseEvent.MOUSE_LAST];

	// MouseListener Methods

	@Override
	public void mousePressed(MouseEvent e) {
		buttons[e.getButton() - 1] = true;
		updateMouseCoords(e);
//		Logger.log("Mouse pressed");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragging = false;
		buttons[e.getButton() - 1] = false;
		updateMouseCoords(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updateMouseCoords(e);
//		Logger.log("Mouse moved");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragging = true;
		updateMouseCoords(e);
	}

	private void updateMouseCoords(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
//		Logger.log("%d, %d", mouseX, mouseY);
	}

	// Getters and Setters

	public static boolean buttonDown(String buttonName) {
		for (MouseMapping b : MouseMapping.values())
			if (b.toString().equalsIgnoreCase(buttonName))
				return buttons[b.button() - 1];
		return false;
	}

	public static int mouseX() {
		return mouseX;
	}

	public static int mouseY() {
		return mouseY;
	}

	public static MouseInput instance() {
		return (null == instance) ? instance = new MouseInput() : instance;
	}

	// Enum Declaration
	enum MouseMapping {

		LEFT_MOUSE(MouseEvent.BUTTON1), RIGHT_MOUSE(MouseEvent.BUTTON3);

		private int button;

		private MouseMapping(int button) {
			this.button = button;
		}

		public int button() {
			return button;
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}

	}

}
