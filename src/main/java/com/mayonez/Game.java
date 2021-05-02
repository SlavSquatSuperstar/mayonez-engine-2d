package com.mayonez;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;

import com.util.Logger;
import com.util.Preferences;

public class Game implements Runnable {

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

	// Window Fields
	private JFrame window;
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
		window = new JFrame(Preferences.TITLE);
		window.setSize(Preferences.WIDTH, Preferences.HEIGHT);
		window.setResizable(false);
		window.setLocationRelativeTo(null); // center in screen
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make sure 'x' button quits program

		// Add input listeners
		window.addKeyListener(keyboard = new KeyInput());
		window.addMouseListener(mouse = new MouseInput());
		window.addMouseMotionListener(mouse);
	}

	// Game Loop Methods

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
				render(window.getGraphics());
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

		stop(0);

	}

	/**
	 * Refreshes all objects in the current scene.
	 * 
	 * @param dt The time elapse since the last frame
	 */
	public void update(double dt) {
		// Poll events
		if (keyboard.keyDown("exit")) {
			running = false;
		}
		if (currentScene != null)
			currentScene.update(dt);
	}

	/**
	 * Repaints all objects in the current scene.
	 */
	// Issue white, sometimes nothing renders at all
	public void render(Graphics g) {
		if (g == null || !window.isVisible())
			return;

		if (img == null || gfx == null) {
			img = window.createImage(window.getWidth(), window.getHeight());
			gfx = img.getGraphics();
		}

		// Render image before drawing to screen
		currentScene.render((Graphics2D) gfx);
		g.drawImage(img, 0, 0, window.getWidth(), window.getHeight(), null);
	}

	// Thread Methods

	/**
	 * Begins running this engine, initializing its thread, displaying the window,
	 * and starting its scene
	 */
	public synchronized void start() {
		if (running) // don't start if already running
			return;

		Logger.log("Engine: Starting");
		running = true;

		// Start thread
		thread = new Thread(this);
		thread.start();

		// Display window and initialize graphics buffer
		window.setVisible(true);
		img = window.createImage(window.getWidth(), window.getHeight());
		gfx = img.getGraphics();

		// Start scene
		if (currentScene != null) {
			currentScene.start();
			Logger.log("Game: Loaded scene \"%s\"", currentScene.getName());
		}
	}

	/**
	 * Shuts down the engine, stopping its thread and hiding its window.
	 */
	public synchronized void stop(int status) {
		running = false;
		Logger.log("Engine: Stopping");

		// Free System resources
		window.setVisible(false);
		window.dispose();

		// Stop thread
		thread.interrupt();
		System.exit(status);
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

	/**
	 * @return the time in seconds since this game started.
	 */
	public static double getTime() {
		return (System.nanoTime() - timeStarted) / 1E9;
	}

}
