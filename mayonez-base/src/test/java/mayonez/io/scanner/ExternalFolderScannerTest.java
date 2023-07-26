package mayonez.io.scanner;

import org.junit.jupiter.api.*;

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

    @Test
    void scanValidFolderIsNotEmpty() {
        var files = scanner.getFiles("src/test/resources/testassets");
        assertFalse(files.isEmpty());
        assertTrue(files.contains("src/test/resources/testassets/text/properties.txt"));
        assertTrue(files.contains("src/test/resources/testassets/images/mario.png"));
        assertFalse(files.contains("src/test/java/mayonez/io/AssetsTest.class"));
        assertFalse(files.contains("src/test/resources/.DS_Store"));
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
