package com.mayonez;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;

import com.util.Logger;
import com.util.Preferences;

// TODO Extend frame?
@SuppressWarnings("serial")
public class Game extends JFrame implements Runnable {

	/*
	 * Field Declarations
	 */

	// Singleton Fields
	private static Game instance;

	// Thread Fields
	private Thread thread;
	private boolean running;

	// Input Fields
	private KeyInput keyboard;
	private MouseInput mouse;

	// Renderer Fields
	private Image img; // Double-buffered Image
	private Graphics gfx;

	// Scene Fields
	private Scene currentScene;

	// Time Fields
	public static int fps = 60;
	public static double timestep = 1.0 / fps;
	public static double timeStarted = System.nanoTime();

	/*
	 * Method Declarations
	 */

	private Game() {
		// Set up the window
		super(Preferences.TITLE);
		setSize(Preferences.WIDTH, Preferences.HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null); // center in screen
		setDefaultCloseOperation(EXIT_ON_CLOSE); // make sure 'x' button quits program

		// Add input listeners
		addKeyListener(keyboard = new KeyInput());
		addMouseListener(mouse = new MouseInput());
		addMouseMotionListener(mouse);
	}

	// Game Loop
	@Override
	public void run() {

		// All time values are in seconds
		double lastTime = 0; // Last time the game loop iterated
		double deltaTime = 0; // Time since last frame

		// For rendering
		boolean ticked = false; // Has engine actually updated?

		// For debugging
		double timer = 0;
		int frames = 0;

		while (running) {

			double currentFrameTime = getTime(); // Time for current frame
			double passedTime = currentFrameTime - lastTime;
			deltaTime += passedTime;
			timer += passedTime;
			lastTime = currentFrameTime; // Reset lastTime

			// Update the game as many times as necessary even if the frame freezes
			while (deltaTime >= timestep) {
				update(deltaTime);
				// Update = Graphics frame rate, FixedUpdate = Physics frame rate
				deltaTime -= timestep;
				ticked = true;

			}

			// Only render if the game has updated to save resources
			if (ticked) {
				render(getGraphics());
				frames++;
				ticked = false;
			}

			// Print ticks and frames each second
			if (timer >= 1) {
				Logger.log("Frames per Second: %d", frames);
				frames = 0;
				timer = 0;
			}

		} // end loop

		stop();

	}

	// Frame Methods

	public void update(double dt) {
		// Poll events
		if (keyboard.keyDown("exit")) {
			running = false;
		}

		if (currentScene != null)
			currentScene.update(dt);
	}

	public void render(Graphics g) {
		if (img == null) {
			img = createImage(getWidth(), getHeight());
			gfx = img.getGraphics();
		}

		// render image before drawing to screen
		currentScene.render((Graphics2D) gfx);

		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

	// Thread Methods

	public synchronized void start() {
		if (running) // don't start if already running
			return;

		Logger.log("Engine: Starting");

		// Display the Window
		requestFocusInWindow();
		setVisible(true);

		// Start the scene
		if (currentScene != null) {
			currentScene.start();
			Logger.log("Game: Loaded scene \"%s\"", currentScene.getName());
		}

		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		if (running)
			running = false;

		// Free System resources
		setVisible(false);
		gfx.dispose();
		dispose();

		Logger.log("Engine: Stopping");
		thread.interrupt();
		System.exit(0);
	}

	// Getters and Setters

	public static Game getGame() { // only create the game once
		// get params from preferences
		return (null == instance) ? instance = new Game() : instance;
	}

	public void changeScene(int scene) {
		switch (scene) {
		case 0:
			this.currentScene = new LevelEditorScene("Level Editor", Preferences.WIDTH, Preferences.HEIGHT);
			break;
		default:
			Logger.log("Game: Unknown scene");
		}

		if (running && currentScene != null)
			currentScene.start();
	}

	public static Scene getCurrentScene() {
		return instance.currentScene;
	}

	public static KeyInput keyboard() {
		return instance.keyboard;
	}

	public static MouseInput mouse() {
		return instance.mouse;
	}

	public static double getTime() {
		return (System.nanoTime() - timeStarted) / 1E9;
	}

}
