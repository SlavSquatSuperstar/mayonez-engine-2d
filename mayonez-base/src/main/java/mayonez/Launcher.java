package mayonez;

import mayonez.util.*;
import mayonez.util.Record;

import java.util.*;

public class Launcher {

    private static final boolean USE_GL_DEFAULT = true;
    private static final boolean SAVE_LOGS_DEFAULT = true;

    private final List<String> argsList;
    private final Record config;

    public Launcher() {
        argsList = new ArrayList<>();
        config = new mayonez.util.Record();
        // TODO set defaults
    }

    public Launcher(String[] args) {
        argsList = readProgramArgs(args);
        config = parseProgramArgs(argsList);
    }

    // System Info Methods

    public OperatingSystem getCurrentOS() {
        return OperatingSystem.getCurrentOS();
    }

    // Run Config Methods

    /**
     * Reads an array of command line in to a list of strings, preserving the order.
     *
     * @param args a string array passed in main()
     * @return the arguments as a list
     */
    public List<String> readProgramArgs(String[] args) {
        return Arrays.asList(args);
    }

    public Record parseProgramArgs(List<String> args) {
        if (args.isEmpty()) return new Record();

        var argMap = new Record();
        for (var i = 0; i < args.size(); i++) {
            var arg = args.get(i);
            if (arg.startsWith("--")) { // is a flag
                var nextArg = getArg(args, i + 1);
                if (nextArg != null && nextArg.startsWith("--")) continue;
                argMap.set(arg.substring(2), nextArg);
                i += 1;
            }
            // ignore positional args
        }
        return argMap;
    }

    private String getArg(List<String> args, int index) {
        if (index >= args.size()) return null;
        return args.get(index);
    }

    public boolean getUseGL() {
        if (!config.contains("engine")) return USE_GL_DEFAULT;

        var glArg = config.getString("engine");
        return switch (glArg) {
            case "" -> throw new IllegalArgumentException("Missing value for option \"engine\"");
            case "gl" -> true;
            case "awt" -> false;
            default -> throw new IllegalArgumentException("Invalid value for option \"engine\"");
        };
    }

    public boolean getSaveLogs() {
        if (!config.contains("log")) return SAVE_LOGS_DEFAULT;

        var logArg = config.getString("log");
        return switch (logArg) {
            case "" -> throw new IllegalArgumentException("Missing value for option \"log\"");
            case "on" -> true;
            case "off" -> false;
            default -> throw new IllegalArgumentException("Invalid value for option \"log\"");
        };
    }

    // Init engine components

    // Load scenes
}
