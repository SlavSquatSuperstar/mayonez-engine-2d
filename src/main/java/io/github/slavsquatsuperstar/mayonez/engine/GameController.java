package io.github.slavsquatsuperstar.mayonez.engine;

import java.awt.event.KeyEvent;

import io.github.slavsquatsuperstar.mayonez.Logger;
import io.github.slavsquatsuperstar.mayonez.input.Input;
import io.github.slavsquatsuperstar.mayonez.levels.Level;

public class GameController implements Runnable {

	// Engine fields
	private Thread thread;
	private boolean running;

	// Display fields
	private Window window;
	private Renderer renderer;

	// Game fields
	private Level level;
	private static int levelWidth, levelHeight;

	// Input
	private Input input;

	// Time
	private static final int FPS = 60;

	public GameController(String title, int width, int height) {
		// level = new Level(width, height);
		window = new Window(title, width, height);
		window.add(renderer = new Renderer(width, height));
		window.addKeyListener(input = new Input());
	}

	/**
	 * The engine's game loop. Uses the semi-fixed timestep approach. <br>
	 * Source 1: https://gafferongames.com/post/fix_your_timestep Source 2:
	 * https://stackoverflow.com/questions/17756521/updating-and-rendering-for-a-2d-game-in-java
	 */
	@Override
	public void run() {

		window.display();

		// All time values are in seconds
		double timeStep = 1 / 60.0; // time between ticks (i.e. 1/60 = 60 FPS)

		double currentTime = 0; // now
		double lastTime = System.nanoTime() / 1E9; // last time the game loop iterated
		double passedTime; // time between current and last
		double unprocessedTime = 0; // time since last tick ("delta" time)

		// For rendering
		boolean ticked = false; // Has the engine actually updated?

		// For debugging
		double timer = 0;
//		int ticks = 0;
		int frames = 0;

		while (running) {

			currentTime = System.nanoTime() / 1E9;
			passedTime = (currentTime - lastTime);
			unprocessedTime += passedTime;
			lastTime = currentTime; // Reset lastTime

			timer += passedTime;

			// Keep updating even if the game freezes
			while (unprocessedTime >= timeStep) {
				update();
				// Update = Graphics frame rate, FixedUpdate = Physics frame rate
				unprocessedTime -= timeStep;
				ticked = true;
//				ticks++;
			}

			// Only render if the game has updated; no use otherwise
			if (ticked) {
				render();
				frames++;
				ticked = false;
			}

			// Print ticks and frames each second
			if (timer >= 1) {
//				Logger.log("Ticks: %d, Frames: %d", ticks, frames);
				Logger.log("Frames Per Second: %d", frames);
//				ticks = 0;
				frames = 0;
				timer = 0;
			}

		} // end loop

		stop();

	} // end run()

	// Level Methods
	public void loadLevel(Level level) {
		this.level = level;
		renderer.setLevel(level);
		levelWidth = level.getWidth();
		levelHeight = level.getHeight();
	}

	// Game methods
	private void update() {
		if (Input.keyDown(KeyEvent.VK_ESCAPE)) {
			running = false;
			return;
		}

		level.update();
	}

	private void render() {
		renderer.render();
	}

	// Thread methods

	/**
	 * Begins running this engine and starts its thread.
	 */
	public synchronized void start() {

		if (!running) {
			Logger.log("Engine: Starting");
			running = true;
			thread = new Thread(this);
			thread.start();
		}

	} // end start()

	/**
	 * Shuts down the engine and stops its thread.
	 */
	public synchronized void stop() {

		if (running)
			running = false;

		window.close();
		Logger.log("Engine: Stopping");
		thread.interrupt();
		System.exit(0);

	} // end stop()

	public static int width() {
		return levelWidth;
	}

	public static int height() {
		return levelHeight;
	}

	public static double deltaTime() {
		return 1.0 / FPS;
	}

}
