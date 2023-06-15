package mayonez.io.scanner;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.scanner.ClasspathFolderScanner} class.
 *
 * @author SlavSquatSuperstar
 */
class ClasspathFolderScannerTest {

    private FolderScanner scanner;

    @BeforeEach
    void createScanner() {
        scanner = new ClasspathFolderScanner();
    }

    @Test
    void scanValidFolderIsNotEmpty() {
        assertFalse(scanner.getFiles("testassets").isEmpty());
    }

    @Test
    void scanInvalidFolderIsEmpty() {
        assertTrue(scanner.getFiles("testasset").isEmpty());
    }

    @Test
    void scanFileIsEmpty() {
        assertTrue(scanner.getFiles("testassets/images/mario.png").isEmpty());
    }

}