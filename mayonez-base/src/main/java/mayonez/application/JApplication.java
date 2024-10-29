package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.keyboard.*;
import mayonez.input.mouse.*;

/**
 * An instance of the application using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JApplication extends Application {

    public JApplication(Window window, KeyManager keyInput, MouseManager mouse) {
        super(window, keyInput, mouse);
    }

    // Application Methods

    @Override
    public float getCurrentTimeSecs() {
        return Time.getTotalProgramSeconds();
    }

    @Override
    public String toString() {
        return String.format("AWT Game (%s)", getRunningString());
    }

}
