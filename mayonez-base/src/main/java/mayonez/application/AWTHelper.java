package mayonez.application;


import mayonez.*;
import mayonez.math.*;

import java.awt.*;
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
     * Attempts to use the specified windowed resolution for full screen
     * instead of the native resolution.
     */
    static void setFullScreenDisplayMode() {
        if (!SCREEN_DEVICE.isDisplayChangeSupported()) return;

        // Find the available resolution closest to the desired one
        var mode = Arrays.stream(SCREEN_DEVICE.getDisplayModes())
                .min(Comparator.comparingInt(AWTHelper::distanceSquared));
        mode.ifPresent(SCREEN_DEVICE::setDisplayMode);
    }

    private static int distanceSquared(DisplayMode mode) {
        var xDiff = mode.getWidth() - Preferences.getScreenWidth();
        var yDiff = mode.getHeight() - Preferences.getScreenHeight();
        return (xDiff * xDiff) + (yDiff * yDiff);
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
