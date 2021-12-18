package slavsquatsuperstar.mayonez.fileio;

import slavsquatsuperstar.mayonez.Logger;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

/**
 * A resource file used by this program.
 *
 * @author SlavSquatSuperstar
 */
public class Asset {

    public final URL path;
    public final boolean isClasspath;

    public Asset(String filename, boolean isClasspath) {
        this.isClasspath = isClasspath;
        this.path = isClasspath ? Assets.getClasspathURL(filename) : Assets.getFileURL(filename);
        System.out.println(path);
    }

    // Getters
    public InputStream inputStream() throws IOException {
        return path.openStream();
    }

    public OutputStream outputStream(boolean append) throws IOException {
        if (isClasspath) {
            Logger.log("%s: Cannot write to classpath resource", getClass().getSimpleName());
            throw new UnsupportedOperationException("Cannot open output stream for classpath asset");
        }
        return FileUtils.openOutputStream(toFile(), append);
    }

    public File toFile() {
        return new File(path.getPath());
    }

    /**
     * Verify that a resource exists at the given URL.
     *
     * @return Whether this {@link Asset} exists.
     */
    public boolean exists() {
        if (isClasspath) {
            try {
                inputStream();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return toFile().exists();
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", getClass().getSimpleName(), path.getPath(), (isClasspath ? "Classpath" : "External"));
    }
}
