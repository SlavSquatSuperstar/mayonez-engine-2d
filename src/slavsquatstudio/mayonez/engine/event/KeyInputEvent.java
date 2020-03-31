package slavsquatstudio.mayonez.engine.event;

public class KeyInputEvent extends GameEvent {

	private int keyCode;
//	private char keyChar;
	private boolean keyDown;

	public KeyInputEvent(int keyCode, boolean keyDown) {
		this.keyCode = keyCode;
		this.keyDown = keyDown;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean isKeyDown() {
		return keyDown;
	}

	@Override
	public String toString() {
		return String.format("Key %d %s", keyCode, keyDown ? "down" : "up");
	}

}
