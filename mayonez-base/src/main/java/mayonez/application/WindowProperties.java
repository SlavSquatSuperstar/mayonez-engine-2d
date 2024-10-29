package mayonez.application;

import mayonez.*;
import mayonez.math.*;

/**
 * Stores different properties of the application window.
 *
 * @author SlavSquatSuperstar
 */
public final class WindowProperties {

    private static Vec2 windowScaling = new Vec2(1);

    private WindowProperties() {
    }

    /**
     * The size of the application window, in pixels.
     *
     * @return the screen size
     */
    public static Vec2 getScreenSize() {
        return new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight());
    }

    /**
     * The content scaling of the application window, usually 1x1 (100%). The
     * value may be different if the current display has changed its screen
     * scaling or resolution.
     * <p>
     * For example, if the window scale is 2x2 (200%), then the window size
     * will appear twice as large as the value stated in the user preferences.
     *
     * @return the window scaling
     */
    public static Vec2 getWindowScaling() {
        return windowScaling;
    }

    static void setWindowScaling(Vec2 windowScaling) {
        WindowProperties.windowScaling = windowScaling;
    }

}
