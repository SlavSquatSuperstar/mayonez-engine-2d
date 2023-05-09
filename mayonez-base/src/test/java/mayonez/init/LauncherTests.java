package mayonez.init;

import mayonez.annotations.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.init.Launcher} class.
 *
 * @author SlavSquatSuperstar
 */
public class LauncherTests {

    private Launcher launcher;

    @BeforeEach
    void createLauncher() {
        launcher = new Launcher();
    }

    // Get Args Test

    @Test
    public void getUseGLTrue() {
        launcher = new Launcher(new String[]{"--engine", "gl"});
        assertTrue(launcher.getUseGL());
        assertEquals(launcher.getEngineType(), EngineType.GL);
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
