package io.github.slavsquatsuperstar.mayonez.engine;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The main display component of the engine.
 */
public class Window extends JFrame {

	public Window(String title, int width, int height) {
		setTitle(title);
		setSize(new Dimension(width, height));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void display() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void close() {
		setVisible(false);
		dispose(); // destroy JFrame object
	}

}