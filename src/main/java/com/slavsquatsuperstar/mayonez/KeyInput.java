package com.slavsquatsuperstar.mayonez;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	// Key Fields
	private boolean[] keys = new boolean[256];

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

	public boolean keyDown(int keyCode) {
		return keys[keyCode];
	}

	/**
	 * Whether the specified {@link KeyMapping} is pressed.
	 * 
	 * @param keyName The name of the {@link KeyMapping}.
	 */
	public boolean keyDown(String keyName) {
		for (KeyMapping m : KeyMapping.values())
			if (m.toString().equalsIgnoreCase(keyName)) // if the desired mapping exists
				return keys[m.keyCode];
		return false;
	}

	public int getAxis(String axisName) {
		for (KeyAxis a : KeyAxis.values())
			if (a.toString().equalsIgnoreCase(axisName))
				return a.value();
		return 0;
	}

	/*
	 * Enum Definitions
	 */

	/**
	 * Stores two keys intended to perform opposite actions.
	 */
	enum KeyAxis {
		
		VERTICAL(KeyMapping.DOWN, KeyMapping.UP), HORIZONTAL(KeyMapping.RIGHT, KeyMapping.LEFT);

		private int posKey, negKey;

		private KeyAxis(KeyMapping posKey, KeyMapping negKey) {
			this.posKey = posKey.keyCode();
			this.negKey = negKey.keyCode();
		}

		/**
		 * @return 1 if the positive key is pressed.<br/>
		 *         -1 if the negative key is pressed.<br/>
		 *         0 if the both or neither key is pressed.<br/>
		 */
		public int value() { // TODO keyboard acceleration?
			// "vector "method to make sure keys don't override each other
			int negComp = (Game.keyboard().keyDown(negKey)) ? -1 : 0;
			int posComp = (Game.keyboard().keyDown(posKey)) ? 1 : 0;
			return negComp + posComp;
		}

		@Override
		public String toString() {
			return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
		}
	}

	/**
	 * Associates a virtual key code to a name.
	 */
	enum KeyMapping {

		// TODO read from file to assign keybinds
		UP(KeyEvent.VK_W), DOWN(KeyEvent.VK_S), LEFT(KeyEvent.VK_A), RIGHT(KeyEvent.VK_D), EXIT(KeyEvent.VK_ESCAPE),
		SPACE(KeyEvent.VK_SPACE), SHIFT(KeyEvent.VK_SHIFT);

		private int keyCode; // TODO allow for multiple?
//		private int[] keyCodes;

		private KeyMapping(int keyCode) {
			this.keyCode = keyCode;
		}

//		private KeyMapping(int... keyCodes) {
//			this.keyCodes = keyCodes;
//		}

		public int keyCode() {
			return keyCode;
		}

//		public int[] getKeyCodes() {
//			return keyCodes;
//		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}

	}

}