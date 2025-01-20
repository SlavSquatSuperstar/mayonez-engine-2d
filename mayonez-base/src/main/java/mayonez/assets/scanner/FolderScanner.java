package mayonez.assets.scanner;

import java.util.*;

/**
 * Recursively searches a resource directory inside the JAR and finds all the files.
 *
 * @author SlavSquatSuperstar
 */
@FunctionalInterface
public interface FolderScanner {

    /**
     * Gets all the files inside the given folder.
     *
     * @param directoryName the path of the folder
     * @return the list of filenames
     */
    List<String> getFiles(String directoryName);

}
