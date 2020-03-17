package mayonez;

import mayonez.engine.Engine;

/**
 * The entry point for the engine.
 */
public class Launcher {
	
	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;
	
	public static final String NAME = "Mayonez Engine";
	public static final String VERSION = "v0.4";

	public static void main(String[] args) {
		Engine engine = new Engine();
		engine.start();
	}

}
