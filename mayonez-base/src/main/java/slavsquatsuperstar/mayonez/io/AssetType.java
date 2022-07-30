package slavsquatsuperstar.mayonez.io;


import slavsquatsuperstar.util.StringUtils;

/**
 * Where in the computer to look for an {@link Asset}, what properties a file receives.
 */
public enum AssetType {
    /**
     * A system resource inside the JAR that is read-only.
     */
    CLASSPATH,
    /**
     * A file outside the JAR and in the local device that can be written to or created.
     */
    EXTERNAL;

    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }
}
