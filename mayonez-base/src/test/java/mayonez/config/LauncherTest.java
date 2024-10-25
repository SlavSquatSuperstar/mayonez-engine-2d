package mayonez.config;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.config.Launcher} class.
 *
 * @author SlavSquatSuperstar
 */
class LauncherTest {

    private Launcher launcher;

    @Test
    void noArgsReturnsDefault() {
        launcher = new Launcher();
        assertTrue(launcher.getUseGL());
    }

    @Test
    void invalidArgsIgnored() {
        launcher = new Launcher(new String[]{"--log", "on"});
        assertTrue(launcher.getUseGL());
    }

    @Test
    void getUseGLTrue() {
        launcher = new Launcher(new String[]{"--engine", "gl"});
        assertTrue(launcher.getUseGL());
    }

    @Test
    void getUseGLFalse() {
        launcher = new Launcher(new String[]{"--engine", "awt"});
        assertFalse(launcher.getUseGL());
    }

    @Test
    void invalidArgValueThrowsException() {
        launcher = new Launcher(new String[]{"--engine", "vk"});
        assertThrows(IllegalArgumentException.class, launcher::getUseGL);
    }

    @Test
    void getUseGLMissingThrowsException() {
        launcher = new Launcher(new String[]{"--engine"});
        assertThrows(IllegalArgumentException.class, launcher::getUseGL);
    }

}
