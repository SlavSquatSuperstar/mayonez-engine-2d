package mayonez.application;


import mayonez.*;

import java.awt.*;
import java.awt.geom.*;

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
        for (var mode : SCREEN_DEVICE.getDisplayModes()) {
            if (mode.getWidth() == Preferences.getScreenWidth()
                    && mode.getHeight() == Preferences.getScreenHeight()) {
                SCREEN_DEVICE.setDisplayMode(mode);
            }
        }
        // Could also find the resolution closest to desired resolution
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
