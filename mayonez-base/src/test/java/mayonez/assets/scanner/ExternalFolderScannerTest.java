package mayonez.assets.scanner;

import mayonez.assets.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.scanner.ExternalFolderScanner} class.
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
        return files.contains(PathUtil.convertPath(filename));
    }

    @Test
    void scanValidFolderIsNotEmpty() {
        var files = scanner.getFiles("src/test/resources/testassets");
        assertFalse(files.isEmpty());
        assertTrue(doesFolderContain(files, "src/test/resources/testassets/text/foo.txt"));
        assertTrue(doesFolderContain(files, "src/test/resources/testassets/out/readme.txt"));
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
