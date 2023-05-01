package mayonez.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.util.OperatingSystem} class.
 *
 * @author SlavSquatSuperstar
 */
public class OperatingSystemTests {

    private final String unixFile = "src/test/resources/testassets/out/info.txt";
    private final String windowsFile = "src\\test\\resources\\testassets\\out\\info.txt";

    @Test
    public void unixToUnixFileCorrect() {
        var filename = OperatingSystem.LINUX.getOSFilename(unixFile);
        assertEquals(unixFile, filename);
    }

    @Test
    public void unixToWindowsFileCorrect() {
        var filename = OperatingSystem.WINDOWS.getOSFilename(unixFile);
        assertEquals(windowsFile, filename);
    }

    @Test
    public void unixToWindowsFolderCorrect() {
        String unixFolder = "src/test/resources/testassets/";
        var filename = OperatingSystem.WINDOWS.getOSFilename(unixFolder);
        String windowsFolderNormalized = "src\\test\\resources\\testassets";
        assertEquals(windowsFolderNormalized, filename);
    }

    @Test
    public void windowsToWindowsFilenameCorrect() {
        var filename = OperatingSystem.WINDOWS.getOSFilename(windowsFile);
        assertEquals(windowsFile, filename);
    }

    @Test
    public void windowsToUnixFileCorrect() {
        var filename = OperatingSystem.LINUX.getOSFilename(windowsFile);
        assertEquals(unixFile, filename);
    }

    @Test
    public void windowsToUnixFolderCorrect() {
        String windowsFolder = "src\\test\\resources\\testassets\\";
        var filename = OperatingSystem.LINUX.getOSFilename(windowsFolder);
        String unixFolderNormalized = "src/test/resources/testassets";
        assertEquals(unixFolderNormalized, filename);
    }

}
