package mayonez.application;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.input.mouse.*;

/**
 * An instance of the application using Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
final class JApplication extends Application {

    public JApplication(Window window, MouseManager mouse) {
        super(window, mouse);
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
