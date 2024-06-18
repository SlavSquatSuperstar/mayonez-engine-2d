package mayonez.config;

import mayonez.util.Record;

import java.util.*;

/**
 * Reads and interprets command-line arguments passed through console commands.
 *
 * @author SlavSquatSuperstar
 */
class ArgumentsParser {

    private final Record programArgs;

    ArgumentsParser() {
        this(new String[0]);
    }

    ArgumentsParser(String[] args) {
        List<String> argsList = readProgramArgs(args);
        programArgs = parseProgramArgs(argsList);
    }

    /**
     * Reads an array of command line in to a list of strings, preserving the order.
     *
     * @param args a string array passed in main()
     * @return the arguments as a list
     */
    List<String> readProgramArgs(String[] args) {
        return Arrays.asList(args);
    }

    /**
     * Parses a {@link mayonez.util.Record} object storing flags and their values from a list of command line arguments.
     *
     * @param args a string list
     * @return the flags and their values
     */
    Record parseProgramArgs(List<String> args) {
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
            // Ignore positional args
        }
        return argMap;
    }

    private String getArg(List<String> args, int index) {
        if (index >= args.size()) return null;
        return args.get(index);
    }

    Record getProgramArgs() {
        return programArgs;
    }
}
