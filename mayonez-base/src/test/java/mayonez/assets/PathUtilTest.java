package mayonez.assets;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.PathUtil} class.
 *
 * @author SlavSquatSuperstar
 */
class PathUtilTest {

    private final String cleanedUnixPath = "foo/bar/baz";
    private final String cleanedWindowsPath = "foo\\bar\\baz";

    @Test
    void cleanUnixPathSuccess() {
        var path = "./foo/./bar/baz/";
        var cleanedPath = PathUtil.convertPath(path, PathUtil.UNIX_SEPARATOR);
        assertEquals(cleanedUnixPath, cleanedPath);
    }

    @Test
    void cleanWindowsPathSuccess() {
        var path = ".\\foo\\.\\bar\\baz\\";
        var cleanedPath = PathUtil.convertPath(path, PathUtil.WINDOWS_SEPARATOR);
        assertEquals(cleanedWindowsPath, cleanedPath);
    }

    @Test
    void convertToUnixPathSuccess() {
        var converted = PathUtil.convertPath(cleanedWindowsPath, PathUtil.UNIX_SEPARATOR);
        assertEquals(cleanedUnixPath, converted);
    }

    @Test
    void convertToWindowsPathSuccess() {
        var converted = PathUtil.convertPath(cleanedUnixPath, PathUtil.WINDOWS_SEPARATOR);
        assertEquals(cleanedWindowsPath, converted);
    }

}
