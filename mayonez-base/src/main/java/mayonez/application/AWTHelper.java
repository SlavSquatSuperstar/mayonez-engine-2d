package mayonez.application;

import mayonez.*;
import mayonez.math.*;

import java.awt.*;
import java.awt.Window;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Assists in graphics context creation and operations for the AWT engine.
 *
 * @author SlavSquatSuperstar
 */
final class AWTHelper {

    final static GraphicsDevice SCREEN_DEVICE = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
    private final static AffineTransform FLIP_XF =
            AffineTransform.getScaleInstance(1.0, -1.0);

    private AWTHelper() {
    }

    // Display Methods

    /**
     * The nearest available full screen resolution to the preferred one, or otherwise the current
     * resolution.
     *
     * @return the best display mode
     */
    static DisplayMode getNearestDisplayMode(WindowConfig config) {
        return Arrays.stream(SCREEN_DEVICE.getDisplayModes())
                .min(Comparator.comparingInt(mode -> getDistanceSquared(mode, config)))
                .orElse(SCREEN_DEVICE.getDisplayMode());
    }

    private static int getDistanceSquared(DisplayMode mode, WindowConfig config) {
        var xDiff = mode.getWidth() - config.width();
        var yDiff = mode.getHeight() - config.height();
        return (xDiff * xDiff) + (yDiff * yDiff);
    }

    /**
     * Set the window fullscreen with the given display mode. The display mode must be valid.
     *
     * @param window the window
     * @param mode   the display mode
     */
    static void setDisplayMode(Window window, DisplayMode mode) {
        if (!SCREEN_DEVICE.isFullScreenSupported()) return;
        SCREEN_DEVICE.setFullScreenWindow(window);

        if (!SCREEN_DEVICE.isDisplayChangeSupported()) return;
        SCREEN_DEVICE.setDisplayMode(mode);
    }

    static Vec2 getWindowContentScale() {
        // Source: https://stackoverflow.com/questions/32586883/windows-scaling
        var screenXf = SCREEN_DEVICE
                .getDefaultConfiguration()
                .getDefaultTransform();
        return new Vec2((float) screenXf.getScaleX(), (float) screenXf.getScaleY());
    }

    // Transform Methods

    /**
     * Creates a transform that flips the window's draw canvas vertically along
     * the middle.
     *
     * @param height the window's height
     * @return the transform
     */
    static AffineTransform getWindowFlipXf(int height) {
        var windowFlipXf = new AffineTransform(FLIP_XF);
        windowFlipXf.translate(0.0, -height);
        return windowFlipXf;
    }

}
