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
        assertFalse(scanner.getFiles("src/test/resources/testassets").isEmpty());
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
