package mayonez.config;

import mayonez.util.Record;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.config.ArgumentsParser} class.
 *
 * @author SlavSquatSuperstar
 */
class ArgumentsParserTest {

    private static final boolean DEFAULT_SAVE_LOGS = true;
    private ArgumentsParser parser;

    @BeforeEach
    void createLauncher() {
        parser = new ArgumentsParser();
    }

    // Parse Args Test

    @Test
    void readArgsCorrect() {
        String[] argsIn = {"--engine", "gl", "--log", "on"};
        List<String> argsOut = List.of("--engine", "gl", "--log", "on");
        assertEquals(argsOut, parser.readProgramArgs(argsIn));
    }

    @Test
    void parseArgsCorrect() {
        List<String> clArgs = List.of("--engine", "gl", "--log", "on");
        var programArgs = parser.parseProgramArgs(clArgs);
        assertEquals("gl", programArgs.getString("engine"));
        assertEquals("on", programArgs.getString("log"));
    }

    @Test
    void parseArgsMissingParamsCorrect() {
        List<String> clArgs = List.of("--engine", "--log", "on");
        var programArgs = parser.parseProgramArgs(clArgs);
        assertEquals("", programArgs.getString("engine"));
        assertEquals("on", programArgs.getString("log"));
    }

    // Old tests from LauncherTest

    @Test
    void getSaveLogsTrue() {
        List<String> clArgs = List.of("--log", "on");
        var programArgs = parser.parseProgramArgs(clArgs);
        assertTrue(getSaveLogs(programArgs));
    }

    @Test
    void getSaveLogsFalse() {
        List<String> clArgs = Collections.emptyList();
        var programArgs = parser.parseProgramArgs(clArgs);
        assertTrue(getSaveLogs(programArgs));
    }

    boolean getSaveLogs(Record programArgs) throws IllegalArgumentException {
        if (!programArgs.contains("log")) return DEFAULT_SAVE_LOGS;

        var logArg = programArgs.getString("log");
        return switch (logArg) {
            case "" -> throw new IllegalArgumentException("Missing value for option \"log\"");
            case "on" -> true;
            case "off" -> false;
            default -> throw new IllegalArgumentException("Invalid value for option \"log\"");
        };
    }

}
