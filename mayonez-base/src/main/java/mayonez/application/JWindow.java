package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.*;
import mayonez.math.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import static mayonez.application.AWTHelper.SCREEN_DEVICE;

/**
 * A window created using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JWindow extends JFrame implements Window {

    // Constants
    private final static int BUFFER_COUNT = 2;

    // Graphics Fields
    private BufferStrategy bs;
    private Graphics2D g2;
    private final AffineTransform windowFlipXf;

    // Window Fields
    private boolean initialized, closedByUser;
    private Point lastPos;
    private Dimension lastSize;
    private final DisplayMode displayMode;

    // Input Fields
    private final JKeyManager keyboard;
    private final JMouseManager mouse;

    /**
     * Create the AWT window.
     *
     * @param config the initialization parameters
     */
    JWindow(WindowConfig config) {
        super(config.title());
        setSize(config.width(), config.height()); // AWT uses total size, unlike GLFW
        getContentPane().setSize(getSize());  // Set content pane size before visible
        // Resizing the content pane plus the window to the preferred size is too buggy
        // TODO mouse coords are off in windowed possibly due to content pane size
        // TODO mouse coords mess up after switching scenes in full screen

        setResizable(config.resizable());
        setLocationRelativeTo(null); // Center in screen

        initialized = false;
        closedByUser = false;
        lastPos = getLocation();
        lastSize = getSize();
        displayMode = AWTHelper.getNearestDisplayMode(config);
        windowFlipXf = AWTHelper.getWindowFlipXf(getHeight());

        // Init as fullscreen or windowed
        setUndecorated(config.fullScreen());
        if (config.fullScreen()) {
            AWTHelper.setDisplayMode(this, displayMode);
        }

        // Set close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closedByUser = true;
                // The close button on the toolbar will notify the game to exit
                // Note that quitting the JVM will not trigger this handler, unlike GLFW
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

        Logger.debug("Created the AWT window");
        Logger.debug("Starting in %s mode", config.fullScreen() ? "full screen" : "windowed");
        Logger.debug("Using full screen size %dx%d", displayMode.getWidth(), displayMode.getHeight());
    }

    // Engine Methods

    @Override
    public void start() {
        if (isVisible()) return;
        setVisible(true);
        initGraphics(); // Initialize graphics resources

        if (!initialized) {
            // Resizing the content pane to the desired size now that it is visible
            // Only do this once
            var titleBarHeight = getHeight() - getContentHeight();
            setSize(getWidth(), getHeight() + titleBarHeight);
            initialized = true;
        }
    }

    @Override
    public void stop() {
        if (!isVisible()) return;
        setVisible(false);
        g2.dispose();
        // Java API docs say the BS doesn't need to be disposed
        dispose();
    }

    // Game Loop Methods

    @Override
    public float getCurrentTimeSecs() {
        return Time.getTotalProgramSeconds();
    }

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
            return; // Need to return or else crashes
        }

        // Use a do-while loop to avoid losing buffer frames
        // Source: https://stackoverflow.com/questions/13590002/understand-bufferstrategy
        do {
            try {
                // Fetch resources
                g2 = (Graphics2D) bs.getDrawGraphics(); // Current buffer's graphics context

                // Clear screen
                g2.clipRect(0, 0, getWidth(), getHeight()); // Render things only in the screen
                g2.clearRect(0, 0, getWidth(), getHeight());

                // Draw
                g2.transform(windowFlipXf); // Flip screen vertically
                SceneManager.renderScene(g2);
            } catch (IllegalStateException e) {
                Logger.error("Error rendering current frame; retrying next frame.");
            } finally {
                // Always flush resources
                g2.dispose();
                bs.show();
            }
        } while (bs.contentsLost());
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
        WindowEvents.WINDOW_EVENTS.broadcast(new WindowResizeEvent(
                getWidth(), getHeight()
        ));

        // Adjust window transform
        windowFlipXf.setTransform(AWTHelper.getWindowFlipXf(getHeight()));
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

        Logger.debug("Set window to %s mode", fullScreen ? "full screen" : "windowed");
    }

    public void setFullScreen() {
        // Save previous position and size
        lastPos = getLocation();
        lastSize = getSize();

        if (isDisplayable()) return; // Must not be visible
        setUndecorated(true);
        AWTHelper.setDisplayMode(this, displayMode);
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
        return super.getWidth(); // AWT messes up if this is changed
    }

    @Override
    public int getHeight() {
        return super.getHeight(); // AWT messes up if this is changed
    }

    @Override
    public int getContentWidth() {
        return getContentPane().getWidth();
    }

    @Override
    public int getContentHeight() {
        return getContentPane().getHeight();
    }

    @Override
    public Vec2 getContentScale() {
        return AWTHelper.getWindowContentScale();
    }

    @Override
    public String toString() {
        return String.format("AWT Window (%s, %dx%d, %s)",
                getTitle(), getWidth(), getHeight(), isFullScreen() ? "Full Screen" : "Windowed");
    }

}
