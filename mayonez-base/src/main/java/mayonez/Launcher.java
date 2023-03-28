package mayonez;

import mayonez.util.*;

import java.util.*;

public class Launcher {

    private static final boolean USE_GL_DEFAULT = true;
    private static final boolean SAVE_LOGS_DEFAULT = true;

    private final List<String> argsList;
    private final Map<String, String> config;

    public Launcher() {
        argsList = new ArrayList<>();
        config = new HashMap<>();
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

    public Map<String, String> parseProgramArgs(List<String> args) {
        if (args.isEmpty()) return new HashMap<>();

        Map<String, String> argMap = new HashMap<>();
        for (var i = 0; i < args.size(); i++) {
            var arg = args.get(i);
            if (arg.startsWith("--")) { // is a flag
                var nextArg = getArg(args, i + 1);
                if (nextArg != null && nextArg.startsWith("--")) continue;
                argMap.put(arg.substring(2), nextArg);
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
        if (!config.containsKey("engine")) return USE_GL_DEFAULT;

        var glArg = config.get("engine");
        if (glArg == null) throw new IllegalArgumentException("Missing value for option \"engine\"");
        else if (glArg.equals("gl")) return true;
        else if (glArg.equals("awt")) return false;
        else throw new IllegalArgumentException("Invalid value for option \"engine\"");
    }

    public boolean getSaveLogs() {
        if (!config.containsKey("log")) return SAVE_LOGS_DEFAULT;

        var logArg = config.get("log");
        if (logArg == null) throw new IllegalArgumentException("Missing value for option \"log\"");
        else if (logArg.equals("on")) return true;
        else if (logArg.equals("off")) return false;
        else throw new IllegalArgumentException("Invalid value for option \"log\"");
    }

    // Init engine components

    // Load scenes
}
