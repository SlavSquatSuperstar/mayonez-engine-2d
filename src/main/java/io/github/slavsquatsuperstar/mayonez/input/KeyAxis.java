package io.github.slavsquatsuperstar.mayonez.input;

public enum KeyAxis {

	VERTICAL(KeyMapping.DOWN, KeyMapping.UP), HORIZONTAL(KeyMapping.RIGHT, KeyMapping.LEFT);

	private int value;
	private int posKeyCode, negKeyCode;
	private boolean posDown, negDown;

	// make reference to key instead of code
	private KeyAxis(KeyMapping posKey, KeyMapping negKey) {
		this.posKeyCode = posKey.keyCode();
		this.negKeyCode = negKey.keyCode();
	}

	// if contains key code
	public boolean match(int keyCode) {
		return keyCode == posKeyCode || keyCode == negKeyCode;
	}

	// receive new key event
	public void setKey(int keyCode, boolean keyDown) {

		if (keyDown) { // every new input will override the old one

			if (keyCode == posKeyCode) {
				posDown = true;
				value = 1;
			} else if (keyCode == negKeyCode) {
				negDown = true;
				value = -1;
			}

		} else { // will remember which keys are down when letting go

			if (keyCode == posKeyCode) {
				posDown = false;
				value = negDown ? -1 : 0;
			} else if (keyCode == negKeyCode) {
				negDown = false;
				value = posDown ? 1 : 0;
			}

		}

	}

	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}

}