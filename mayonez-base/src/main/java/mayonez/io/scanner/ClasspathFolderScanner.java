package mayonez.io.scanner;

import mayonez.io.*;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;

/**
 * Scans a classpath folder in the jar for resources.
 *
 * @author SlavSquatSuperstar
 */
public class ClasspathFolderScanner implements FolderScanner {

    /**
     * Recursively searches a resource directory inside the JAR and adds all
     * assets.
     * <p>
     * Sources:
     * <ul>
     *     <li><a href="https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory">StackOverflow</a></li>
     *     <li><a href="https://github.com/ronmamo/reflections"></a>org.reflections</li>
     * </ul>
     *
     * @param directoryName a folder inside the jar
     */
    @Override
    public List<String> getFiles(String directoryName) {
        var directory = FilePath.getOSFilename(directoryName);
        var resources = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(directory)
                        .setScanners(Scanners.Resources))
                .get(Scanners.Resources.with(".*\\.*"));
        return new ArrayList<>(resources);
    }

}
