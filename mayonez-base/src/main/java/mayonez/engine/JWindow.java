package mayonez.engine;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.input.keyboard.*;
import mayonez.input.mouse.*;

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

    // Input Fields
    private KeyManager keyboard;
    private MouseManager mouse;

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
    }

    @Override
    public void endFrame() {
        keyboard.updateKeys();
        mouse.updateMouse();
    }

    // Render Methods

    @Override
    public void render() {
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
                SceneManager.renderCurrentScene(g2);
                flushResources();
            } while (bs.contentsLost());
        } catch (IllegalStateException e) {
            Logger.warn("Error rendering current frame; retrying next frame.");
        }
    }

    private void initGraphics() {
        if (!isVisible()) return;
        try {
            createBufferStrategy(BUFFER_COUNT);
            bs = getBufferStrategy();
        } catch (IllegalStateException e) {
            Logger.warn("Error initializing window graphics; retrying next frame.");
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
    public void setKeyInput(KeyManager keyboard) {
        this.keyboard = keyboard;
        addKeyListener(keyboard);
    }

    @Override
    public void setMouseInput(MouseManager mouse) {
        this.mouse = mouse;
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    // Getters

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

//    public boolean isFullScreen() {
//        var env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        var device = env.getDefaultScreenDevice();
//        return device.getFullScreenWindow() != null;
//    }

    @Override
    public String toString() {
        return String.format("AWT Window (%s, %dx%d)", getTitle(), getWidth(), getHeight());
    }

}
