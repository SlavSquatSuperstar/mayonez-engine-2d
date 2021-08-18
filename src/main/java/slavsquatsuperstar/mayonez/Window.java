package slavsquatsuperstar.mayonez;

import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

public class Window extends JFrame {

    public Window(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make sure 'x' button quits program
        createDisplay();
    }

    protected void createDisplay() {}

    // Game Loop Methods

    public void start() {
        setLocationRelativeTo(null); // center in screen
        setVisible(true);
    }

    public void stop() {
        setVisible(false);
        dispose();
    }

    // Input Methods

    public void addKeyInput(KeyAdapter keyboard) {
        addKeyListener(keyboard);
    }

    public void addMouseInput(MouseAdapter mouse) {
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

}
