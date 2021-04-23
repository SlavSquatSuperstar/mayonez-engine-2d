package mayonez;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import util.Logger;

public class Game implements Runnable {

	/*
	 * Field Declarations
	 */

	// Singleton Fields
	private static Game instance;

	// Thread Fields
	private Thread thread;
	private boolean running;

	// Window Fields
	private JFrame window;
	private int width, height;
	private String title;

	// Renderer Fields
	private Canvas renderer;
	private BufferStrategy bs;
	private Graphics g;

	// Image Fields
//	private BufferedImage image;
//	private int[] pixels;

	// Scene Fields
	private Scene currentScene;

	// Time Fields
	public static int fps = 60;
	public static double deltaTime = 1.0 / fps;
	public static double timeStarted = System.nanoTime();

	/*
	 * Method Declarations
	 */

	private Game() {
		width = 1080;
		height = 720;
		title = "Mayonez Engine";
	}

	private void init() {
		// Set up the window
		window = new JFrame(title);
		Dimension size = new Dimension(width, height);
		window.setSize(size);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make sure 'x' button quits program

		// Set up and add the renderer
		renderer = new Canvas();
		renderer.setPreferredSize(size);
		renderer.setMaximumSize(size);
		renderer.setMinimumSize(size);
		renderer.setFocusable(false);
		window.add(renderer);

		// Add input listeners
		window.addKeyListener(KeyInput.instance());
		window.addMouseListener(MouseInput.instance());
		window.addMouseMotionListener(MouseInput.instance());
		renderer.addMouseListener(MouseInput.instance());
		renderer.addMouseMotionListener(MouseInput.instance());

		// Display the window
		window.setLocationRelativeTo(null); // center window in screen
		window.requestFocusInWindow(); // make sure window gets input focus
		window.setVisible(true);

		// Set up the graphics components
		/*
		 * Do this before starting to prevent the white "flash" when nothing has been
		 * drawn yet Do this after making window visible to prevent
		 * IllegalStateException: Component must have a valid peer
		 */
		renderer.createBufferStrategy(3);
		bs = renderer.getBufferStrategy();
		g = bs.getDrawGraphics();

//		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		// Start the scene
		if (null != currentScene)
			currentScene.start();
	}

	// Update Methods

	public void update() {
		if (null != currentScene)
			currentScene.update();
	}

	public void render() {
		// Don't render if the window is invisible;
		if (null == bs || null == g || !renderer.isVisible()) {
			// Set up the graphics components
			renderer.createBufferStrategy(3);
			bs = renderer.getBufferStrategy();
			g = bs.getDrawGraphics();
			return;
		}

		// Clear the screen
		g.clearRect(0, 0, width, height);

		// Draw the background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// g.drawImage(image, 0, 0, width, height, null);

		// Draw all textures
		if (null != currentScene)
			currentScene.render(g);

		bs.show();
	}

	// Game Loop
	@Override
	public void run() {

		init();

		// All time values are in seconds
		double currentTime = 0; // now
		double lastTime = getTime(); // last time the game loop iterated
		double passedTime; // time between current and last
		double unprocessedTime = 0; // time since last tick ("delta" time)

		// For rendering
		boolean ticked = false; // Has the engine actually updated?

		// For debugging
		double timer = 0;
		// int ticks = 0;
		int frames = 0;

		while (running) {

			// poll events
			if (KeyInput.keyDown("exit")) {
				running = false;
				break;
			}

			currentTime = getTime();
			passedTime = (currentTime - lastTime);
			unprocessedTime += passedTime;
			lastTime = currentTime; // Reset lastTime

			timer += passedTime;

			// Update the game as many times as necessary even if the frame freezes
			while (unprocessedTime >= deltaTime) {
				update();
				// Update = Graphics frame rate, FixedUpdate = Physics frame rate
				unprocessedTime -= deltaTime;
				ticked = true;
				// ticks++;
			}

			// Only render if the game has updated to save resources
			if (ticked) {
				render();
				frames++;
				ticked = false;
			}

			// Print ticks and frames each second
			if (timer >= 1) {
				// Logger.log("Ticks: %d, Frames: %d", ticks, frames);
				Logger.log("FPS: %d", frames);
				// ticks = 0;
				frames = 0;
				timer = 0;
			}

		} // end loop

		stop();

	}

	// Thread Methods

	public synchronized void start() {
		if (!running) { // don't start if already running
			Logger.log("Engine: Starting");
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	public synchronized void stop() {
		if (running)
			running = false;

		window.setVisible(false);
		window.dispose();

		Logger.log("Engine: Stopping");
		thread.interrupt();
		System.exit(0);
	}

	// Getters and Setters

	public void loadScene(Scene newScene) {
		currentScene = newScene;
//		if (running)
//			currentScene.start(); // starts twice
	}

	public static Scene getCurrentScene() {
		return getGame().currentScene;
	}

	public static Game getGame() { // only create the game once
		return (null == instance) ? instance = new Game() : instance;
	}

	public static double getTime() {
		return (System.nanoTime() - timeStarted) / 1.0E9;
	}

}
