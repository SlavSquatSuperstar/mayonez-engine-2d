package mayonez.event;

import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import mayonez.input.KeyInput;
import mayonez.level.Level;

public class EventHandler implements Observer {

	private Level level;

	public EventHandler(Level level) {
		this.level = level;
	}

	@Override
	public void update(Observable o, Object arg) {

//		System.out.println(arg);

		if (o instanceof KeyInput && arg instanceof KeyInputEvent) {

			KeyInputEvent event = (KeyInputEvent) arg;

			if (event.isKeyDown()) {

				switch (event.getKeyCode()) {

				case KeyEvent.VK_W:
					level.applyForce(0, -50);
					break;
				case KeyEvent.VK_S:
					level.applyForce(0, 50);
					break;
				case KeyEvent.VK_A:
					level.applyForce(-50, 0);
					break;
				case KeyEvent.VK_D:
					level.applyForce(50, 0);
					break;

				case KeyEvent.VK_ESCAPE:
					System.exit(0);
					break;
				}

			} else {

				switch (event.getKeyCode()) {

				case KeyEvent.VK_W:
					level.applyForce(0, 0);
					break;
				case KeyEvent.VK_S:
					level.applyForce(0, 0);
					break;
				case KeyEvent.VK_A:
					level.applyForce(0, 0);
					break;
				case KeyEvent.VK_D:
					level.applyForce(0, 0);
					break;

				case KeyEvent.VK_ESCAPE:
					System.exit(0);
					break;
				}

			}

		}

	}

}
