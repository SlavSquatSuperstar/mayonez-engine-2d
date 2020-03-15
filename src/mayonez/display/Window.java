package mayonez.display;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import mayonez.input.KeyInput;
import mayonez.input.MouseInput;
import mayonez.level.Level;

/**
 * The main display component of the engine.
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	private Renderer renderer;

	public Window(String title, int width, int height) {
		setTitle(title);
		setSize(new Dimension(width, height));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		createView();
	}

	public synchronized void addInputListeners(KeyInput k, MouseInput m) {
		addKeyListener(k);
		renderer.addMouseListener(m);
		renderer.addMouseMotionListener(m);
		renderer.addMouseWheelListener(m);
	}

	private void createView() {
		System.out.println("Window: Creating view");
		renderer = new Renderer(getWidth(), getHeight());
		add(renderer, BorderLayout.CENTER);
	}

	public void display() {
		setLocationRelativeTo(null);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}

	public void render(Level l) {
		if (!isVisible())
			return;

		renderer.render(l);
	}

}
