package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
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
    private final static AffineTransform FLIP_XF =
            AffineTransform.getScaleInstance(1.0, -1.0);
    private final static GraphicsDevice SCREEN_DEVICE = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();

    // Window Fields
    private BufferStrategy bs;
    private Graphics2D g2;
    private boolean closedByUser;
    private Point lastPos;
    private Dimension lastSize;

    // Input Fields
    private final JKeyManager keyboard;
    private final JMouseManager mouse;

    /**
     * Create the AWT window.
     *
     * @param title  the window title
     * @param width  the window width
     * @param height the window height
     */
    JWindow(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setResizable(true);
        setLocationRelativeTo(null); // Center in screen

        lastPos = getLocation();
        lastSize = getSize();

        // Set close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closedByUser = true; // Red 'x' button should notify game to exit
            }
        });

        // Set resize listener
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onWindowResized();
            }
        });

        // Add input listeners
        keyboard = new JKeyManager();
        addKeyListener(keyboard);

        mouse = new JMouseManager();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    // Engine Methods

    @Override
    public void start() {
        if (isVisible()) return;
        setVisible(true);
        initGraphics(); // Initialize graphics resources
    }

    @Override
    public void stop() {
        if (!isVisible()) return;
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
        KeyInput.updateKeys();
        MouseInput.updateMouse();
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
                SceneManager.renderScene(g2);
                flushResources();
            } while (bs.contentsLost());
        } catch (IllegalStateException e) {
            Logger.error("Error rendering current frame; retrying next frame.");
        }
    }

    private void initGraphics() {
        if (!isVisible()) return;
        try {
            createBufferStrategy(BUFFER_COUNT);
            bs = getBufferStrategy();
        } catch (IllegalStateException e) {
            Logger.error("Error initializing window graphics; retrying next frame.");
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
    public KeyInputHandler getKeyInputHandler() {
        return keyboard;
    }

    @Override
    public MouseInputHandler getMouseInputHandler() {
        return mouse;
    }

    // Full Screen Methods

    private void onWindowResized() {
        System.out.println("resized");
        System.out.printf("window = %dx%d\n", getSize().width, getSize().height);
        System.out.printf("content = %dx%d\n", getContentPane().getSize().width, getContentPane().getSize().height);
        System.out.printf("location = %d, %d\n", getLocation().x, getLocation().y);
    }

    @Override
    public boolean isFullScreen() {
        return SCREEN_DEVICE.getFullScreenWindow() != null;
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        stop();
        if (fullScreen) setFullScreen();
        else setWindowed();
        start();
    }

    // Note that macOS native full screen is different from Swing full screen
    public void setFullScreen() {
        // Save previous position and size
        lastPos = getLocation();
        lastSize = getSize();

        if (isDisplayable()) return; // Must not be visible
        setUndecorated(true);

        if (!SCREEN_DEVICE.isFullScreenSupported()) return;
        SCREEN_DEVICE.setFullScreenWindow(this);
    }

    public void setWindowed() {
        if (isDisplayable()) return; // Must not be visible
        setUndecorated(false);
        SCREEN_DEVICE.setFullScreenWindow(null);

        // Restore previous size and position
        // Works rather inconsistently
        setLocation(lastPos);
        setSize(lastSize);
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

    @Override
    public String toString() {
        return String.format("AWT Window (%s, %dx%d)", getTitle(), getWidth(), getHeight());
    }

}
