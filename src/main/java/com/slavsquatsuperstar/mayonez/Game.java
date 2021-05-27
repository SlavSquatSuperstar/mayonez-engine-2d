package com.slavsquatsuperstar.mayonez;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.slavsquatsuperstar.game.LevelEditorScene;
import com.slavsquatsuperstar.util.Constants;
import com.slavsquatsuperstar.util.Logger;

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
	private int width, height;

	// Renderer Fields
	private Graphics g;
	private BufferStrategy bs;

	// Scene Fields
	private Scene currentScene;

	// Time Fields
	public static float timestep = 1.0f / Constants.FPS;
	public static long timeStarted = System.nanoTime();

	/*
	 * Method Declarations
	 */

	private Game() {
		// Set up the window
		window = new JFrame(Constants.SCREEN_TITLE);
		width = Constants.SCREEN_WIDTH;
		height = Constants.SCREEN_HEIGHT;
		window.setSize(width, height);
		window.setResizable(false);
		window.setLocationRelativeTo(null); // center in screen
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make sure 'x' button quits program

		// Add input listeners
		keyboard = new KeyInput();
		mouse = new MouseInput();
		window.addKeyListener(keyboard);
		window.addMouseListener(mouse);
		window.addMouseMotionListener(mouse);

		initGraphics();
	}

	// Game Loop Methods

	@Override
	public void run() {

		// All time values are in seconds
		float lastTime = 0; // Last time the game loop iterated
		float deltaTime = 0; // Time since last frame

		// For rendering
		boolean ticked = false; // Has engine actually updated?

		// For debugging
		float timer = 0;
		int frames = 0;

		while (running) {

			float currentFrameTime = getTime(); // Time for current frame
			float passedTime = currentFrameTime - lastTime;
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
//				render(window.getGraphics());
				render();
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
	public void update(float dt) {
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
	// Issue: white flicked, sometimes nothing renders at all
	public void render() {

		if (bs == null) {
			initGraphics();
			return;
		}

		/*
		 * Use a do-while loop to avoid lose buffer frames Source:
		 * https://stackoverflow.com/questions/13590002/understand-bufferstrategy
		 */
		do {
			// Clear the screen
			g = bs.getDrawGraphics();
			g.clearRect(0, 0, width, height);

			if (null != currentScene)
				currentScene.render((Graphics2D) g);

			g.dispose();
			bs.show();
		} while (bs.contentsLost());

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
		g.dispose();

		// Stop thread
		thread.interrupt();
		System.exit(status);
	}

	// Getters and Setters

	public static Game instance() { // only create the game once
		// get params from preferences
		return (null == instance) ? instance = new Game() : instance;
	}

	public void changeScene(int scene) {
		switch (scene) {
		case 0:
			this.currentScene = new LevelEditorScene("Level Editor", Constants.SCREEN_WIDTH * 2,
					Constants.SCREEN_HEIGHT * 2);
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
	public static float getTime() {
		return (System.nanoTime() - timeStarted) / 1.0E9f;
	}

	public static boolean isFullScreen() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = ge.getDefaultScreenDevice();
		return device.getFullScreenWindow() != null;
	}

	private void initGraphics() {
		if (window == null || !window.isVisible())
			return;

		window.createBufferStrategy(2);
		bs = window.getBufferStrategy();
//		g = bs.getDrawGraphics();
	}

}
