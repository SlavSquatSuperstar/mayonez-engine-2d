package mayonez.io;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.OperatingSystem} class.
 *
 * @author SlavSquatSuperstar
 */
class OperatingSystemTest {

    private final String unixFileCleaned = "src/test/resources/testassets/out/readme.txt";
    private final String windowsFileCleaned = "src\\test\\resources\\testassets\\out\\readme.txt";

    @Test
    void unixToUnixFileCorrect() {
        var filename = OperatingSystem.LINUX.getOSFilename("./src/test/resources/testassets/out/readme.txt");
        assertEquals(unixFileCleaned, filename);
    }

    @Test
    void unixToWindowsFileCorrect() {
        var filename = OperatingSystem.WINDOWS.getOSFilename("./src/test/resources/testassets/out/readme.txt");
        assertEquals(windowsFileCleaned, filename);
    }

    @Test
    void unixToWindowsFolderCorrect() {
        var filename = OperatingSystem.WINDOWS.getOSFilename("./src/test/resources/testassets/");
        String windowsDirCleaned = "src\\test\\resources\\testassets";
        assertEquals(windowsDirCleaned, filename);
    }

    @Test
    void windowsToWindowsFilenameCorrect() {
        var filename = OperatingSystem.WINDOWS.getOSFilename(".\\src\\test\\resources\\testassets\\out\\readme.txt");
        assertEquals(windowsFileCleaned, filename);
    }

    @Test
    void windowsToUnixFileCorrect() {
        var filename = OperatingSystem.LINUX.getOSFilename(".\\src\\test\\resources\\testassets\\out\\readme.txt");
        assertEquals(unixFileCleaned, filename);
    }

    @Test
    void windowsToUnixFolderCorrect() {
        var filename = OperatingSystem.LINUX.getOSFilename(".\\src\\test\\resources\\testassets\\");
        String unixDirCleaned = "src/test/resources/testassets";
        assertEquals(unixDirCleaned, filename);
    }

}
