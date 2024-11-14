package mayonez.assets;

import java.io.File;
import java.nio.file.Path;

/**
 * Converts file paths for different operating and file systems.
 *
 * @author SlavSquatSuperstar
 */
public final class PathUtil {

    /**
     * The file separator on the user's current operating system.
     */
    public static final String CURRENT_SEPARATOR = File.separator;

    /**
     * The file separator on Unix systems, which is a forward slash '/'.
     */
    public static final String UNIX_SEPARATOR = "/";

    /**
     * The file separator on Windows systems, which is a backward slash '\'.
     */
    public static final String WINDOWS_SEPARATOR = "\\";

    /**
     * The file separator for the Java classpath system, which is a forward slash '/'.
     */
    public static final String CLASSPATH_SEPARATOR = UNIX_SEPARATOR;

    private PathUtil() {
    }

    /**
     * Cleans the given path string and converts it to the target file system.
     * This method assumes no file separators are part of the actual path component
     * names. Note that '/' and '\' are valid filename characters in Unix, and may be
     * incorrectly replaced.
     *
     * @param path          the path string
     * @param fileSeparator the file separator
     * @return the converted path string
     */
    public static String convertPath(String path, String fileSeparator) {
        // Get the path string on the current file system
        var currentPath = path.replaceAll("[/\\\\]", CURRENT_SEPARATOR);
        // Need to remove trailing separators so ClassLoader doesn't give error
        var currentPathCleaned = Path.of(currentPath).normalize().toString();
        // Get the path string on the target file system
        return currentPathCleaned.replace(CURRENT_SEPARATOR, fileSeparator);
    }

    /**
     * Gets the path with the correct file separators for the current OS.
     *
     * @param path the path string
     * @return the current OS path string
     */
    public static String convertPath(String path) {
        return convertPath(path, CURRENT_SEPARATOR);
    }

}
