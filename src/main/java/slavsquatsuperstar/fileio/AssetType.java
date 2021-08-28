package slavsquatsuperstar.fileio;

/**
 * Where to look for an {@link Asset} and how to treat it.
 */
public enum AssetType {
    /**
     * A file inside the JAR which cannot be modified.
     */
    CLASSPATH,
    /**
     * An file in the external file system which can be written to or created.
     */
    LOCAL;
//    /**
//     * Retrieved from the internet and cannot be modified
//     */
//    WEB

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
