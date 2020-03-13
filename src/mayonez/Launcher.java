package mayonez;

import mayonez.engine.Engine;

/**
 * The entry point for the engine.
 */
public class Launcher {

	public static void main(String[] args) {
		Engine engine = new Engine();
		engine.start();
	}

}
