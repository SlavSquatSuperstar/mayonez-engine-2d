package mayonez.io;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.LocationType} class.
 *
 * @author SlavSquatSuperstar
 */
public class LocationTypeTest {

    // Classpath Location Tests

    @Test
    void validClasspathURLNotNull() {
        var classpathFileValid = "testassets/text/foo.txt";
        assertNotNull(LocationType.CLASSPATH.getURL(classpathFileValid));
    }

    @Test
    void invalidClasspathURLIsNull() {
        var classpathFileInvalid = "testassets/text/bar.txt";
        assertNull(LocationType.CLASSPATH.getURL(classpathFileInvalid));
    }

    // External Location Tests

    @Test
    void anyExternalURLNotNull() {
        var externalFileValid =
                PathUtil.convertPath("src/test/resources/testassets/text/foo.txt");
        var externalFileInvalid =
                PathUtil.convertPath("src/test/resources/testassets/text/bar.txt");

        assertNotNull(LocationType.EXTERNAL.getURL(externalFileValid));
        assertNotNull(LocationType.EXTERNAL.getURL(externalFileInvalid));
    }

}
