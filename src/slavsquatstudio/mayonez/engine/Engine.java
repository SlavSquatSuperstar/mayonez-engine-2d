package slavsquatstudio.mayonez.engine;

import slavsquatstudio.mayonez.game.Game;

/**
 * The backbone of the engine that updates all the components.
 */
public class Engine implements Runnable {

	// Engine fields
	private Thread thread;
	private boolean running;

	private Game game;

	public Engine() {
		game = new Game();
	}

	/**
	 * The engine's game loop. Uses the semi-fixed timestep approach. <br>
	 * See: <a href=
	 * "https://gafferongames.com/post/fix_your_timestep/">https://gafferongames.com/post/fix_your_timestep/</a>
	 */
	@Override
	public void run() {

		// All time values are in seconds
		double tickTime = 1.0 / 60; // time-step

		double currentTime = 0;
		double lastTime = System.nanoTime() / 1E9;
		double unprocessedTime = 0; // delta time

		// For debugging
		double timer = 0;
		int ticks = 0;
		int frames = 0;

		while (running) {

			boolean render = false;

			currentTime = System.nanoTime() / 1E9;
			double passedTime = (currentTime - lastTime);

			unprocessedTime += passedTime;
			timer += passedTime;
			lastTime = currentTime;

			// Keep updating even if the game freezes
			while (unprocessedTime >= tickTime) {
				game.update();
				unprocessedTime -= tickTime;
				render = true;
				ticks++;
			}

			// Only render if the game has updated
			if (render) {
				game.render();
				frames++;
			} // sleep?

			// Print ticks and frames each second
			if (timer >= 1) {
				System.out.printf("Ticks: %d, Frames: %d\n", ticks, frames);
				ticks = 0;
				frames = 0;
				timer = 0;
			}

		}

		stop();

	}

	// Thread methods

	/**
	 * Begins running this engine and starts its thread.
	 */
	public synchronized void start() {
		if (!running) {
			System.out.println("Engine: Starting");
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Shuts down the engine and stops its thread.
	 */
	public synchronized void stop() {
		if (running) {
			running = false;

			try {
				System.out.println("Engine: Stopping");
				thread.join();
				System.exit(0);
			} catch (InterruptedException e) {
				System.out.println("Engine: Error shutting down");
				e.printStackTrace();
			}
		}
	}

}
