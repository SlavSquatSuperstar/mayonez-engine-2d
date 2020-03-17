package mayonez.game;

import mayonez.Launcher;
import mayonez.display.Window;
import mayonez.event.EventHandler;
import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.level.Level;
import mayonez.object.Square;

/**
 * The main logic component of the engine that communicates between the display
 * and level.
 */
public class Game {

	// Display
	private Window window;

	// Event
	private EventHandler handler;

	// Input
	private KeyInput keys;
	private MouseInput mouse;

	// Level
	private Level level;

	// State

	public Game() {
		
		level = new Level();
		for (int i = 0; i < 16; i++)
			level.addObject(new Square());
		
		handler = new EventHandler(level);

		keys = new KeyInput(handler);
		mouse = new MouseInput(handler);

		window = new Window(Launcher.NAME + " " + Launcher.VERSION, Launcher.WIDTH, Launcher.HEIGHT);
		window.addInputListeners(keys, mouse);
		window.display();
		
	}

	public void update() {
		level.update();
	}

	public void render() {
		window.render(level);
	}

}
