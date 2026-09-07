package mayonez.config;

import mayonez.application.Backend;
import mayonez.application.WindowLibrary;
import mayonez.graphics.EngineType;
import mayonez.util.Record;
import org.apache.commons.cli.*;

/**
 * Reads and interprets command-line arguments passed through console commands.
 *
 * @author SlavSquatSuperstar
 */
class ArgumentsParser {

    final static Backend AWT_BACKEND
            = new Backend("AWT", WindowLibrary.AWT, EngineType.AWT);
    final static Backend GL_BACKEND
            = new Backend("GLFW/OpenGL", WindowLibrary.GLFW, EngineType.GL);
    final static Backend DEFAULT_BACKEND = GL_BACKEND;

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
     * Parse the application {@link Backend} from the command line.
     *
     * @param cl the command line
     * @return the backend
     */
    Backend getBackend(CommandLine cl) {
        var engine = cl.getOptionValue("engine");
        if (engine == null) {
            return DEFAULT_BACKEND;
        } else if (engine.equals("gl")) {
            return GL_BACKEND;
        } else if (engine.equals("awt")) {
            return AWT_BACKEND;
        } else {
            throw new IllegalArgumentException("Invalid argument %s for engine".formatted(engine));
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

    // Helper Methods

}
