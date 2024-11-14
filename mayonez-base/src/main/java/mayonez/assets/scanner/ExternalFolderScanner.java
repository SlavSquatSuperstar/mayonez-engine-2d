package mayonez.assets.scanner;

import mayonez.assets.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;

/**
 * Scans an external folder on the computer for resources.
 *
 * @author SlavSquatSuperstar
 */
public class ExternalFolderScanner implements FolderScanner {

    /**
     * Recursively searches an external directory inside the JAR and adds all
     * assets.
     * <p>
     * Source: <a href="https://www.logicbig.com/how-to/java/find-classpath-files-under-folder-and-sub-folder.html">LogicBig</a>
     *
     * @param directoryName a folder inside the jar
     */
    @Override
    public List<String> getFiles(String directoryName) {
        var directory = PathUtil.convertPath(directoryName);
        if (!(new File(directory).isDirectory())) return new ArrayList<>(); // If not directory return empty list

        // Use Files.walk() to get recursive tree
        var path = Paths.get(directory);
        try (Stream<Path> files = Files.walk(path)) {
            return files.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(n -> !n.contains(".DS_Store"))
                    .toList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
