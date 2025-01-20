package mayonez.assets.scanner;

import mayonez.assets.*;
import org.reflections.vfs.Vfs;

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
        var resources = new ArrayList<String>();

        // Check folder exists
        var dirUrl = new ClasspathFilePath(directoryName).getURL();
        if (dirUrl == null) return resources;

        try (var dir = Vfs.fromURL(dirUrl)) {
            for (var file : dir.getFiles()) {
                if (file.getName().contains(".DS_Store")) {
                    continue;
                }
                var path = file.getRelativePath();
                resources.add("%s/%s".formatted(directoryName, path));
            }
            return resources;
        } catch (Exception e) {
            return resources;
        }
    }

}
