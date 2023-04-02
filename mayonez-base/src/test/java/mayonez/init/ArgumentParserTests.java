package mayonez.init;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ArgumentsParser} class.
 *
 * @author SlavSquatSuperstar
 */
public class ArgumentParserTests {

    private ArgumentsParser parser;

    @BeforeEach
    void createLauncher() {
        parser = new ArgumentsParser();
    }

    // Parse Args Test

    @Test
    public void readArgsCorrect() {
        String[] argsIn = {"--engine", "gl", "--log", "on"};
        List<String> argsOut = List.of("--engine", "gl", "--log", "on");
        assertEquals(argsOut, parser.readProgramArgs(argsIn));
    }

    @Test
    public void parseArgsCorrect() {
        List<String> argsList = List.of("--engine", "gl", "--log", "on");
        var argsMap = parser.parseProgramArgs(argsList);
        assertEquals("gl", argsMap.getString("engine"));
        assertEquals("on", argsMap.getString("log"));
    }

    @Test
    public void parseArgsMissingParamsCorrect() {
        List<String> argsList = List.of("--engine", "--log", "on");
        var argsMap = parser.parseProgramArgs(argsList);
        assertEquals("", argsMap.getString("engine"));
        assertEquals("on", argsMap.getString("log"));
    }

}