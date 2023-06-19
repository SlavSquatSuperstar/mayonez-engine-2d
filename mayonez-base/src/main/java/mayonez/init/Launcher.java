package mayonez.init;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.util.Record;

/**
 * Initializes all engine components, parses through the main method
 * program arguments, and starts the application.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
public class Launcher {

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
        // TODO load scenes
    }

    // Run Config Methods

    boolean getUseGL() throws IllegalArgumentException {
        if (!programArgs.contains("engine")) return RunConfig.DEFAULT_USE_GL;

        var glArg = programArgs.getString("engine");
        return switch (glArg) {
            case "" -> throw new IllegalArgumentException("Missing value for option \"engine\"");
            case "gl" -> true;
            case "awt" -> false;
            default -> throw new IllegalArgumentException("Invalid value for option \"engine\"");
        };
    }

    public void setRunConfig() {
        var config = new RunConfig(getUseGL());
        Mayonez.setConfig(config);
    }

}
