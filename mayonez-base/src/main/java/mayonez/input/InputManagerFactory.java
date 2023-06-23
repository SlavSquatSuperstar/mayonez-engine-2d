package mayonez.input;

import mayonez.*;

/**
 * A factory class that constructs {@link mayonez.input.MouseManager} objects
 * depending on the run configuration.
 *
 * @author SlavSquatSuperstar
 */
public class InputManagerFactory {

    private InputManagerFactory() {
    }

    /**
     * Creates a new {@link mayonez.input.MouseManager} object.
     *
     * @return the mouse input manager
     */
    public static MouseManager createMouseInput() {
        if (Mayonez.getUseGL()) return new GLMouseManager();
        else return new JMouseManager();
    }

}
