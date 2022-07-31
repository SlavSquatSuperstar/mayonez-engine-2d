package slavsquatsuperstar.mayonez.io

import slavsquatsuperstar.util.StringUtils.capitalize

/**
 * Where in the computer to look for an [Asset], what properties a file receives.
 */
enum class AssetType {
    /**
     * A system resource inside the JAR that is read-only.
     */
    CLASSPATH,

    /**
     * A file outside the JAR and in the local device that can be written to or created.
     */
    EXTERNAL;

    override fun toString(): String = capitalize(name)
}