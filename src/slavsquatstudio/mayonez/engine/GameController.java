package slavsquatstudio.mayonez.engine;

import slavsquatstudio.mayonez.engine.display.GameWindow;
import slavsquatstudio.mayonez.engine.event.EventHandler;
import slavsquatstudio.mayonez.engine.input.KeyInput;
import slavsquatstudio.mayonez.engine.input.MouseInput;
import slavsquatstudio.mayonez.engine.level.Level;

/**
 * The main logic component of the engine that communicates between the display
 * and level.
 */
public abstract class GameController {

	// Display
	protected GameWindow window;

	// Event
	protected EventHandler handler;

	// Input
	protected KeyInput keys;
	protected MouseInput mouse;

	// Level
	protected Level level;

	// State

	public GameController() {

	}

	public void update() {
		level.update();
	}

	public void render() {
		window.render(level);
	}

}
