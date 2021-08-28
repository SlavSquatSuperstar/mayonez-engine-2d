package slavsquatsuperstar.fileio;

import org.apache.commons.lang3.StringUtils;

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
        return StringUtils.capitalize(name().toLowerCase());
    }
}
