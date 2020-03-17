package mayonez.event;

public class KeyInputEvent2 extends Event {
	
	private String eventType;
	private char keyChar;
	private int keyCode;

	public KeyInputEvent2(String eventType, char keyChar, int keyCode) {
		this.eventType = eventType;
		this.keyChar = keyChar;
		this.keyCode = keyCode;
	}

	public String getEventType() {
		return eventType;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public char getKeyChar() {
		return keyChar;
	}

	@Override
	public String toString() {
		if (eventType.equalsIgnoreCase("Typed"))
			return String.format("KeyInput: %s \"%c\"", eventType, keyChar);
		else
			return String.format("KeyInput: %s %d", eventType, keyCode);
	}

}
