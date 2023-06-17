package mayonez.engine;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.input.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * The display component for the game, using AWT.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JWindow extends JFrame implements Window {

    // Constants
    private final static int BUFFER_COUNT = 2;
    private final static AffineTransform FLIP_XF = AffineTransform.getScaleInstance(1.0, -1.0);

    // Window Fields
    private BufferStrategy bs;
    private Graphics2D g2;
    private boolean closedByUser;

    JWindow(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closedByUser = true; // red 'x' button will notify game to exit
            }
        });
    }

    // Engine Methods

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

    // Game Loop Methods

    @Override
    public boolean notClosedByUser() {
        return !closedByUser;
    }

    @Override
    public void beginFrame() {
        if (KeyInput.keyDown("escape")) closedByUser = true;
    }

    @Override
    public void endFrame() {
        KeyInput.endFrame();
        MouseInput.endFrame();
    }

    // Render Methods

    private void initGraphics() {
        if (!isVisible()) return;
        try {
            createBufferStrategy(BUFFER_COUNT);
            bs = getBufferStrategy();
        } catch (IllegalStateException e) {
            Logger.error("Error initializing window graphics; retrying next frame.");
        }
    }

    @Override
    public void render(Scene scene) {
        if (bs == null) {
            initGraphics();
            return;
        }
        try {
            // Use a do-while loop to avoid losing buffer frames
            // Source: https://stackoverflow.com/questions/13590002/understand-bufferstrategy
            do {
                clearScreen();
                flipScreenVertically();
                scene.render(g2); // Draw scene
                flushResources();
            } while (bs.contentsLost());
        } catch (IllegalStateException e) {
            Logger.debug("Error rendering current frame; retrying next frame.");
        }
    }

    private void clearScreen() {
        g2 = (Graphics2D) bs.getDrawGraphics();
        g2.clipRect(0, 0, getWidth(), getHeight()); // Render things only in the screen
        g2.clearRect(0, 0, getWidth(), getHeight());
    }

    private void flipScreenVertically() {
        g2.transform(FLIP_XF);
        g2.translate(0, -getHeight());
    }

    private void flushResources() {
        g2.dispose();
        bs.show();
    }

    // Input Methods

    @Override
    public void setKeyInput(KeyInput keyboard) {
        addKeyListener(keyboard);
    }

    @Override
    public void setMouseInput(MouseInput mouse) {
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    // Properties

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public String toString() {
        return String.format("AWT Window (%s, %dx%d)", getTitle(), getWidth(), getHeight());
    }

}
