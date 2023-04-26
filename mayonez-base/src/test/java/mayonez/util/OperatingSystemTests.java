package mayonez.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.util.OperatingSystem} class.
 *
 * @author SlavSquatSuperstar
 */
public class OperatingSystemTests {

    private final String unixFile = "src/test/resources/testassets/out/info.txt";
    private final String unixFolder = "src/test/resources/testassets/";
    private final String unixFolderNormalized = "src/test/resources/testassets";
    private final String windowsFile = "src\\test\\resources\\testassets\\out\\info.txt";
    private final String windowsFolder = "src\\test\\resources\\testassets\\";
    private final String windowsFolderNormalized = "src\\test\\resources\\testassets";

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
        var filename = OperatingSystem.WINDOWS.getOSFilename(unixFolder);
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
        var filename = OperatingSystem.LINUX.getOSFilename(windowsFolder);
        assertEquals(unixFolderNormalized, filename);
    }

}
