package mayonez.display;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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

	public void render() {
		if (!isVisible())
			return;

		renderer.render();
	}

}
