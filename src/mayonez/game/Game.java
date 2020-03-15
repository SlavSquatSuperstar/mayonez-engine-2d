package mayonez.game;

import java.util.Observable;
import java.util.Observer;

import mayonez.Launcher;
import mayonez.display.Window;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.level.Level;
import mayonez.object.Square;

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

	// Level
	private Level level;

	// State

	public Game() {
		keys = new KeyInput(this);
		mouse = new MouseInput(this);

		level = new Level();
		for (int i = 0; i < 10; i++)
			level.addObject(new Square());

		window = new Window("Mayonez Engine v0.3", Launcher.WIDTH, Launcher.HEIGHT);
		window.addInputListeners(keys, mouse);
		window.display();
	}

	public void update() {
		level.update();
	}

	public void render() {
		window.render(level);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(arg);
	}

}
