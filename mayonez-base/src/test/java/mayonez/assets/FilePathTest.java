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
    void validClasspathPathIsClasspath() {
        var path = FilePath.of("testassets/text/foo.txt");
        assertInstanceOf(ClasspathFilePath.class, path);
    }

    @Test
    void invalidClasspathPathIsExternal() {
        var path = FilePath.of("testassets/text/bar.txt");
        assertInstanceOf(ExternalFilePath.class, path);
    }


    @Test
    void validExternalPathIsExternal() {
        var path = FilePath.of("src/test/resources/testassets/text/foo.txt");
        assertInstanceOf(ExternalFilePath.class, path);
    }

    @Test
    void invalidExternalPathIsExternal() {
        var path = FilePath.of("src/test/resources/testassets/text/bar.txt");
        assertInstanceOf(ExternalFilePath.class, path);
    }

}
