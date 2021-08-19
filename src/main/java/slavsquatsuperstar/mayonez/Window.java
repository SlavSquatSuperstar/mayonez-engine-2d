package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.renderer.Renderable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferStrategy;

/**
 * The display component for the game.
 *
 * @author SlavSquatSuperstar
 */
public class Window extends JFrame implements IGameWindow {

    private BufferStrategy bs;
    private Graphics2D g2;

    public Window(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make sure 'x' button quits program
    }

    // Game Loop Methods

    @Override
    public void start() {
        setLocationRelativeTo(null); // center in screen
        setVisible(true);
        initGraphics(); // initialize graphics resources
    }

    @Override
    public void stop() {
        setVisible(false);
        g2.dispose();
        dispose();
    }

    // Render Methods

    private void initGraphics() {
        if (!isVisible())
            return;
        try {
            createBufferStrategy(2);
            bs = getBufferStrategy();
        } catch (IllegalStateException e) {
            Logger.log("Engine: Error initializing window graphics; trying again next frame.");
        }
    }

    @Override
    public void render(Renderable r) {
        if (bs == null) {
            initGraphics();
            return;
        }

        try {
            // Use a do-while loop to avoid losing buffer frames
            // Source: https://stackoverflow.com/questions/13590002/understand-bufferstrategy
            do {
                g2 = (Graphics2D) bs.getDrawGraphics();
                g2.clearRect(0, 0, getWidth(), getHeight()); // Clear the screen

                r.render(g2); // Draw scene

                g2.dispose(); // Flush Resources
                bs.show();
            } while (bs.contentsLost());
        } catch (IllegalStateException e) {
            Logger.log("Engine: Error rendering screen; trying again next frame.");
        }
    }

    // Input Methods

    @Override
    public void addKeyInput(KeyAdapter keyboard) {
        addKeyListener(keyboard);
    }

    @Override
    public void addMouseInput(MouseAdapter mouse) {
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

}
