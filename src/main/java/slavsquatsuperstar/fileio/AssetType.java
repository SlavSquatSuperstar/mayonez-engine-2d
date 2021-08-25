package slavsquatsuperstar.fileio;

/**
 * Where to look for an [Asset] and how to treat it.
 */
public enum AssetType {
    /**
     * Found inside the JAR and cannot be modified.
     */
    CLASSPATH,
    /**
     * Found in the external file system and can be modified.
     */
    LOCAL,
    /**
     * An empty file outside the JAR created upon use
     */
    OUTPUT,
//    /**
//     * Retrieved from the internet and cannot be modified
//     */
//    WEB
}
