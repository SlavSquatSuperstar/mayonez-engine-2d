package io.github.slavsquatsuperstar.mayonez.input;

import java.awt.event.KeyEvent;

public enum KeyMapping { // allow for adding keybinds?

	UP(KeyEvent.VK_W), DOWN(KeyEvent.VK_S), LEFT(KeyEvent.VK_A), RIGHT(KeyEvent.VK_D), EXIT(KeyEvent.VK_ESCAPE);

	private int keyCode; // allow mulitple

	private KeyMapping(int keyCode) {
		this.keyCode = keyCode;
		// KeyEvent.getKeyText(keyCode);
	}

	public int keyCode() {
		return keyCode;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

}