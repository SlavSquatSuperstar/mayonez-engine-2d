package mayonez.input;

import java.awt.event.KeyEvent;
import java.util.Observer;

import mayonez.event.KeyInputEvent;

public class KeyInput extends InputListener {

	public KeyInput(Observer o) {
		super(o);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		publish(new KeyInputEvent(e.getKeyCode(), true));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		publish(new KeyInputEvent(e.getKeyCode(), false));
	}

}
