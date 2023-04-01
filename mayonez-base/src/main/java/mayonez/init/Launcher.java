package mayonez.init;

import mayonez.*;
import mayonez.util.*;
import mayonez.util.Record;

/**
 * Initializes all engine components, sets preferences through the main method program arguments,
 * and starts the application.
 *
 * @author SlavSquatSuperstar
 */
public class Launcher {

    private static final boolean USE_GL_DEFAULT = true;
    private static final boolean SAVE_LOGS_DEFAULT = true;
    private final Record programArgs;

    /**
     * Create a launcher the application without passing in any arguments.
     */
    public Launcher() {
        this(new String[0]);
    }

    /**
     * Create a launcher the application and passes in the program arguments.
     *
     * @param args a string array
     */
    public Launcher(String[] args) {
        programArgs = new ArgumentsParser(args).getProgramArgs();
        // TODO set run config
        // TODO init engine components
        // TODO read preferences
        // TODO load scenes
    }

    // System Info Methods

    public OperatingSystem getCurrentOS() {
        return OperatingSystem.getCurrentOS();
    }

    // Run Config Methods

    boolean getUseGL() throws IllegalArgumentException {
        if (!programArgs.contains("engine")) return USE_GL_DEFAULT;

        var glArg = programArgs.getString("engine");
        return switch (glArg) {
            case "" -> throw new IllegalArgumentException("Missing value for option \"engine\"");
            case "gl" -> true;
            case "awt" -> false;
            default -> throw new IllegalArgumentException("Invalid value for option \"engine\"");
        };
    }

    boolean getSaveLogs() throws IllegalArgumentException {
        if (!programArgs.contains("log")) return SAVE_LOGS_DEFAULT;

        var logArg = programArgs.getString("log");
        return switch (logArg) {
            case "" -> throw new IllegalArgumentException("Missing value for option \"log\"");
            case "on" -> true;
            case "off" -> false;
            default -> throw new IllegalArgumentException("Invalid value for option \"log\"");
        };
    }

    // Run Config Methods

    public void setRunConfig() {
        Mayonez.setUseGL(getUseGL());
    }

}
