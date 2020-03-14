package mayonez.game;

import java.util.Observable;
import java.util.Observer;

import mayonez.display.Window;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;

/**
 * The main logic component of the engine that communicates between the display
 * and level.
 */
public class Game implements Observer {

	// Display
	private Window window;

	// Event

	// Input
	private KeyInput keys;
	private MouseInput mouse;

	// State

	public Game() {
		keys = new KeyInput(this);
		mouse = new MouseInput(this);

		window = new Window("Mayonez Engine v0.2", 480, 360);
		window.addInputListeners(keys, mouse);
		window.display();
	}

	public void update() {

	}

	public void render() {
		window.render();
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(arg);
	}

}
