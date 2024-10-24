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
        var files = scanner.getFiles("testassets");
        assertFalse(files.isEmpty());
        assertTrue(files.contains("testassets/text/foo.txt"));
        assertTrue(files.contains("testassets/out/readme.txt"));
        assertFalse(files.contains("mayonez/io/AssetsTest.class"));
        assertFalse(files.contains(".DS_Store"));
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