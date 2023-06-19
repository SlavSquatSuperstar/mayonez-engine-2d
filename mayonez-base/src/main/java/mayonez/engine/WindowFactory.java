package mayonez.engine;

import mayonez.*;

/**
 * A factory class that constructs {@link Window} objects
 * depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
final class WindowFactory {

    private WindowFactory() {
    }

    /**
     * Creates a new game engine object with the given engine type.
     *
     * @param useGL whether to use OpenGL instead of Java's AWT library
     * @return the game engine
     */
    static Window createWindow(boolean useGL) {
        var title = String.format("%s %s (%s)",
                (useGL ? "GL" : "AWT"),
                Preferences.getTitle(), Preferences.getVersion()
        );
        var width = Preferences.getScreenWidth();
        var height = Preferences.getScreenHeight();

        if (useGL) return new GLWindow(title, width, height);
        else return new JWindow(title, width, height);
    }

}
