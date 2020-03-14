package mayonez.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observer;

public class KeyInput extends InputListener implements KeyListener {

	public KeyInput(Observer o) {
		super(o);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		publish("Pressed " + e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		publish("Released " + e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		publish("Typed " + e.getKeyChar());
	}

}
