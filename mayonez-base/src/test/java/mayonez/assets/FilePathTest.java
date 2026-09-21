package mayonez.assets;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.FilePath} class.
 *
 * @author SlavSquatSuperstar
 */
class FilePathTest {

    // Auto-Guess Location Tests

    @Test
    void validClasspathFilenameIsClasspath() {
        var path = FilePath.of("testassets/text/foo.txt");
        assertInstanceOf(ClasspathFilePath.class, path);
    }

    @Test
    void invalidClasspathFilenameIsExternal() {
        var path = FilePath.of("testassets/text/bar.txt");
        assertInstanceOf(ExternalFilePath.class, path);
    }


    @Test
    void validExternalFilenameIsExternal() {
        var path = FilePath.of("src/test/resources/testassets/text/foo.txt");
        assertInstanceOf(ExternalFilePath.class, path);
    }

    @Test
    void invalidExternalFilenameIsExternal() {
        var path = FilePath.of("src/test/resources/testassets/text/bar.txt");
        assertInstanceOf(ExternalFilePath.class, path);
    }

}
