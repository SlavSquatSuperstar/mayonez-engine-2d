package mayonez.io.scanner;

import mayonez.io.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.scanner.ExternalFolderScanner} class.
 *
 * @author SlavSquatSuperstar
 */
class ExternalFolderScannerTest {

    private FolderScanner scanner;

    @BeforeEach
    void createScanner() {
        scanner = new ExternalFolderScanner();
    }

    private static boolean doesFolderContain(List<String> files, String filename) {
        return files.contains(OperatingSystem.getCurrentOSFilename(filename));
    }

    @Test
    void scanValidFolderIsNotEmpty() {
        var files = scanner.getFiles("src/test/resources/testassets");
        assertFalse(files.isEmpty());
        assertTrue(doesFolderContain(files, "src/test/resources/testassets/text/properties.txt"));
        assertTrue(doesFolderContain(files, "src/test/resources/testassets/images/mario.png"));
        assertFalse(doesFolderContain(files, "src/test/java/mayonez/io/AssetsTest.class"));
        assertFalse(doesFolderContain(files, "src/test/resources/.DS_Store"));
    }

    @Test
    void scanInvalidFolderIsEmpty() {
        assertTrue(scanner.getFiles("src/test/resources/testasset").isEmpty());
    }

    @Test
    void scanFileIsEmpty() {
        assertTrue(scanner.getFiles("src/test/resources/testassets/images/mario.png").isEmpty());
    }

}
