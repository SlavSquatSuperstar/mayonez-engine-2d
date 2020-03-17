package mayonez.event;

import java.awt.event.MouseEvent;

public class MouseInputEvent2 extends Event {

	private MouseEvent event;

	public MouseInputEvent2(MouseEvent e) {
		this.event = e;
	}

	public String getEventType() {
		switch (event.getID()) {

		case MouseEvent.MOUSE_CLICKED:
			return "Clicked";
		case MouseEvent.MOUSE_PRESSED:
			return "Pressed";
		case MouseEvent.MOUSE_RELEASED:
			return "Released";
		default:
			return null;
		}
	}

	public String getButton() {
		switch (event.getButton()) {
		case MouseEvent.BUTTON1:
			return "left mouse";
		case MouseEvent.BUTTON3:
			return "right mouse";
		default:
			return "mouse";
		}
	}

	public int getX() {
		return event.getX();
	}

	public int getY() {
		return event.getY();
	}

	@Override
	public String toString() {
		return String.format("MouseInput: %s %s at %d, %d", getEventType(), getButton(), getX(), getY());
	}
}
