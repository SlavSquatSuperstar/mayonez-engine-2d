package mayonez.io.scanner;

import mayonez.io.*;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.*;
import java.util.regex.*;

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
     * Source: <a href="https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory">StackOverflow</a>
     *
     * @param directoryName a folder inside the jar
     */
    @Override
    public List<String> getFiles(String directoryName) {
        var directory = FilePath.getOSFilename(directoryName);
        var resources = new Reflections(directory, Scanners.Resources)
                .getResources(Pattern.compile(".*\\.*"));
        return new ArrayList<>(resources);
    }

}
