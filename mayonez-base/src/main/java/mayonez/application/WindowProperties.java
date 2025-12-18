package mayonez.application;

import mayonez.*;
import mayonez.math.*;

/**
 * Stores different properties of the application window.
 *
 * @author SlavSquatSuperstar
 */
public final class WindowProperties {

    private WindowProperties() {
    }

    /**
     * The size of the application window, in screen units.
     *
     * @return the screen size
     */
    public static Vec2 getScreenSize() {
        return new Vec2(Preferences.getScreenWidth(), Preferences.getScreenHeight());
    }

}
