package mayonez.event;

import java.awt.event.KeyEvent;

public class KeyInputEvent3 extends Event {

	private KeyEvent event;

	public KeyInputEvent3(KeyEvent e) {
		this.event = e;
	}

	public String getEventType() {
		switch (event.getID()) {

		case KeyEvent.KEY_PRESSED:
			return "Pressed";
		case KeyEvent.KEY_RELEASED:
			return "Released";
		case KeyEvent.KEY_TYPED:
			return "Typed";
		default:
			return null;

		}
	}
	
	public boolean isEventType(String type) {
		return type.equalsIgnoreCase(getEventType());
	}

	public int getKeyCode() {
		return event.getKeyCode();
	}

	public char getKeyChar() {
		return event.getKeyChar();
	}

	@Override
	public String toString() {
		if (getEventType() == "Typed")
			return String.format("KeyInput: %s \"%c\"", getEventType(), getKeyChar());
		else
			return String.format("KeyInput: %s %d", getEventType(), getKeyCode());
	}

}
