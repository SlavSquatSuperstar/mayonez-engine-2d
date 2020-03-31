package slavsquatstudio.mayonez.game;

import slavsquatstudio.mayonez.engine.GameController;
import slavsquatstudio.mayonez.engine.display.GameWindow;
import slavsquatstudio.mayonez.engine.event.EventHandler;
import slavsquatstudio.mayonez.engine.input.KeyInput;
import slavsquatstudio.mayonez.engine.input.MouseInput;
import slavsquatstudio.mayonez.engine.level.Level;
import slavsquatstudio.mayonez.game.objects.SquareObject;

/**
 * The main logic component of the engine that communicates between the display
 * and level.
 */
public class Game extends GameController {

	public Game() {

		level = new Level();
		for (int i = 0; i < 16; i++)
			level.addObject(new SquareObject());

		handler = new EventHandler(level);

		keys = new KeyInput(handler);
		mouse = new MouseInput(handler);

		window = new GameWindow(Launcher.NAME + " " + Launcher.VERSION, Launcher.WIDTH, Launcher.HEIGHT);
		window.addInputListeners(keys, mouse);
		window.display();

	}

}
