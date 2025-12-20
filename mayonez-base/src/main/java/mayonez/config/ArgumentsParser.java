package mayonez.config;

import mayonez.util.Record;
import org.apache.commons.cli.*;

/**
 * Reads and interprets command-line arguments passed through console commands.
 *
 * @author SlavSquatSuperstar
 */
class ArgumentsParser {

    private final CommandLineParser parser;
    private final Options options;

    ArgumentsParser() {
        parser = new DefaultParser();

        options = new Options();
        options.addOption(Option.builder("e")
                .longOpt("engine")
                .hasArg()
                .desc("The engine windowing/rendering backend")
                .get());
        options.addOption(Option.builder("g")
                .longOpt("glfallback")
                .hasArg(false)
                .desc("Use the old OpenGL version")
                .get());
    }

    // Parser Methods

    /**
     * Parse the command line from the arguments.
     *
     * @param args the arguments
     * @return the command line
     */
    CommandLine parse(String[] args) {
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the application {@link RunConfig} from the parsed command line.
     *
     * @param cl the command line
     * @return the run config
     */
    RunConfig getRunConfig(CommandLine cl) {
        var useGL = parseUseGL(cl.getOptionValue("engine"));
        var glFallback = cl.hasOption("glfallback");
        return new RunConfig(useGL, glFallback);
    }

    /**
     * Create a {@link Record} storing the parsed command line.
     *
     * @param cl the command line
     * @return the record
     */
    Record serialize(CommandLine cl) {
        var record = new Record();
        for (var opt : cl.getOptions()) {
            // Prefer long option name as key
            var key = opt.getLongOpt() != null ? opt.getLongOpt() : opt.getOpt();
            var values = cl.getOptionValues(opt);

            if (values == null) {
                record.set(key, true);  // Flag
            } else if (values.length == 1) {
                record.set(key, values[0]); // One arg
            } else {
                record.set(key, values); // Var args
            }
        }
        return record;
    }

    // Helper Methods

    private static boolean parseUseGL(String engine) {
        if (engine == null) return RunConfig.DEFAULT_USE_GL;
        else if (engine.equals("gl")) return true;
        else if (engine.equals("awt")) return false;
        else throw new IllegalArgumentException("Invalid argument %s for engine".formatted(engine));
    }

}
