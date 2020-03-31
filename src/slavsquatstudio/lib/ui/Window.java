package slavsquatstudio.lib.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public abstract class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	public Window(String title, int width, int height) {

		setTitle(title);
		setSize(new Dimension(width, height));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		createView();

	}

	public void display() {

		setLocationRelativeTo(null);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});

	}

	// Abstract Methods
	protected abstract void createView();

	protected abstract void update();

}
