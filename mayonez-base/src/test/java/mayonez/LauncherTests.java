package mayonez;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.Launcher} class.
 *
 * @author SlavSquatSuperstar
 */
public class LauncherTests {

    private Launcher launcher;

    @BeforeEach
    void createLauncher() {
        launcher = new Launcher();
    }

    // Parse Args Test

    @Test
    public void readArgsCorrect() {
        String[] argsIn = {"--engine", "gl", "--log", "on"};
        List<String> argsOut = List.of("--engine", "gl", "--log", "on");
        assertEquals(argsOut, launcher.readProgramArgs(argsIn));
    }

    @Test
    public void parseArgsCorrect() {
        List<String> argsList = List.of("--engine", "gl", "--log", "on");
        Map<String, String> argsMap = launcher.parseProgramArgs(argsList);
        assertEquals("gl", argsMap.get("engine"));
        assertEquals("on", argsMap.get("log"));
    }

    @Test
    public void parseArgsMissingParamsCorrect() {
        List<String> argsList = List.of("--engine", "--log", "on");
        Map<String, String> argsMap = launcher.parseProgramArgs(argsList);
        assertNull(argsMap.get("engine"));
        assertEquals("on", argsMap.get("log"));
    }

    // Get Args Test

    @Test
    public void getUseGLTrue() {
        launcher = new Launcher(new String[]{"--engine", "gl"});
        assertTrue(launcher.getUseGL());
    }

    @Test
    public void getUseGLFalse() {
        launcher = new Launcher(new String[]{"--engine", "awt"});
        assertFalse(launcher.getUseGL());
    }

    @Test
    public void getUseGLDefaultTrue() {
        launcher = new Launcher(new String[]{"--log", "on"});
        assertTrue(launcher.getUseGL());
    }

    @Test
    public void getUseGLInvalidThrowsException() {
        launcher = new Launcher(new String[]{"--engine", "vk"});
        assertThrows(IllegalArgumentException.class, launcher::getUseGL);
    }

    @Test
    public void getUseGLMissingThrowsException() {
        launcher = new Launcher(new String[]{"--engine"});
        assertThrows(IllegalArgumentException.class, launcher::getUseGL);
    }

    @Test
    public void getSaveLogsTrue() {
        launcher = new Launcher(new String[]{"--log", "on"});
        assertTrue(launcher.getSaveLogs());
    }

    @Test
    public void getSaveLogsFalse() {
        launcher = new Launcher(new String[]{"--log", "off"});
        assertFalse(launcher.getSaveLogs());
    }

}
