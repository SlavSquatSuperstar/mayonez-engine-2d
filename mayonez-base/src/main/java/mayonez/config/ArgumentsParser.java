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
        return new RunConfig(useGL);
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
            var values = cl.getOptionValues(opt);
            if (values.length == 1) {
                record.set(opt.getKey(), values[0]);
            } else {
                record.set(opt.getKey(), values);
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
