package mayonez.config;

import mayonez.application.Backend;
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
        options.addOption(Option.builder("b")
                .longOpt("backend")
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
     * Parse the application {@link Backend} from the command line.
     *
     * @param cl the command line
     * @return the backend
     */
    Backend getBackend(CommandLine cl) {
        var backend = cl.getOptionValue("backend");
        if (backend == null) {
            return Backend.DEFAULT;
        } else if (backend.equals("gl")) {
            return Backend.GL;
        } else if (backend.equals("awt")) {
            return Backend.AWT;
        } else {
            throw new IllegalArgumentException("Invalid argument %s for backend".formatted(backend));
        }
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

}
