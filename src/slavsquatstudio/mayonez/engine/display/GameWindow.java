package slavsquatstudio.mayonez.engine.display;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import slavsquatstudio.mayonez.engine.input.KeyInput;
import slavsquatstudio.mayonez.engine.input.MouseInput;
import slavsquatstudio.mayonez.engine.level.Level;

/**
 * The main display component of the engine.
 */
public class GameWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private GameRenderer renderer;

	public GameWindow(String title, int width, int height) {
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
		renderer = new GameRenderer(getWidth(), getHeight());
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
