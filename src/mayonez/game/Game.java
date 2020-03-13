package mayonez.game;

import mayonez.display.Window;

/**
 * The main logic component of the engine that communicates between the display
 * and level.
 */
public class Game {

	private Window window;

	public Game() {
		window = new Window("Mayonez Engine v0.1", 480, 360);
		window.display();
	}

	public void update() {

	}

	public void render() {
		window.render();
	}

}
