package io.github.slavsquatsuperstar.mayonez.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

// key adapter for just keys, key listener if accommodate mouse
// TODO input smoothing?
public class Input implements KeyListener {

	private static HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	private static HashMap<String, Boolean> mappings = new HashMap<String, Boolean>();

	// Key methods
	public static boolean keyDown(int keyCode) {
		return Boolean.TRUE.equals(keys.get(keyCode));
	}

	// TODO
	public static boolean keyDown(String keyName) {
		for (KeyMapping m : KeyMapping.values())
			if (m.toString().equalsIgnoreCase(keyName))
				return Boolean.TRUE.equals(mappings.get(m.toString()));
		return false;
	}

	// Enum methods

	/*
	 * @return the input value of the given axis
	 */
	public static int getAxis(String name) {
		Optional<KeyAxis> optional = Arrays.stream(KeyAxis.values()).filter(axis -> axis.toString().equals(name))
				.findFirst();
		return optional.isEmpty() ? 0 : optional.get().value();
	}

	// KeyListener methods

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		keys.put(e.getKeyCode(), true);

		for (KeyAxis a : KeyAxis.values()) {
			if (a.match(code)) {
				a.setKey(code, true);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		keys.remove(e.getKeyCode());

		for (KeyAxis a : KeyAxis.values()) {
			if (a.match(code)) {
				a.setKey(code, false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}