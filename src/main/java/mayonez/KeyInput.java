package mayonez;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	// Singleton Field
	private static KeyInput instance;

	// Key Fields
	private static boolean[] keys = new boolean[KeyEvent.KEY_LAST];

	// KeyListener Methods

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		keys[code] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		keys[code] = false;
	}

	// Getters and Setters

	public static boolean keyDown(int keyCode) {
		return keys[keyCode];
	}

	public static boolean keyDown(String keyName) {
		for (KeyMapping m : KeyMapping.values()) {
			if (m.toString().equalsIgnoreCase(keyName)) // if the desired mapping exists
				return keys[m.keyCode];
		}
		return false;
	}

	public static int getAxis(String axisName) {
		for (KeyAxis a : KeyAxis.values()) {
			if (a.toString().equalsIgnoreCase(axisName))
				return a.value();
		}
		return 0;
	}

	public static KeyInput instance() {
		return (null == instance) ? instance = new KeyInput() : instance;
	}

	/*
	 * Enum Definitions
	 */

	// TODO keyboard acceleration
	enum KeyAxis {

		VERTICAL(KeyMapping.DOWN, KeyMapping.UP), HORIZONTAL(KeyMapping.RIGHT, KeyMapping.LEFT);

		private int posKey, negKey;

		// make reference to key instead of code
		private KeyAxis(KeyMapping posKey, KeyMapping negKey) {
			this.posKey = posKey.keyCode();
			this.negKey = negKey.keyCode();
		}

//		// if contains key code
//		public boolean match(int keyCode) {
//			return keyCode == posKeyCode || keyCode == negKeyCode;
//		}

		public int value() {
			// "vector "method to make sure keys don't override each other
			int negComp = (KeyInput.keyDown(negKey)) ? -1 : 0;
			int posComp = (KeyInput.keyDown(posKey)) ? 1 : 0;
			return negComp + posComp;
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}

	}

	enum KeyMapping {

		// TODO read from file to assign keybinds
		UP(KeyEvent.VK_W), DOWN(KeyEvent.VK_S), LEFT(KeyEvent.VK_A), RIGHT(KeyEvent.VK_D), EXIT(KeyEvent.VK_ESCAPE),
		SPACE(KeyEvent.VK_SPACE);

		private int keyCode; // allow for multiple?

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

}