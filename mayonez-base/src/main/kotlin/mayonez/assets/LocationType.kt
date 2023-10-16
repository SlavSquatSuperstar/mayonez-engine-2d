package mayonez.assets

import mayonez.util.*

/**
 * The location of an [Asset] in the computer, which defines its file
 * permissions.
 */
enum class LocationType {
    /** A system resource inside the JAR that is read-only. */
    CLASSPATH,

    /**
     * A file outside the JAR and in the local device that is readable and
     * writable.
     */
    EXTERNAL;

    override fun toString(): String = StringUtils.capitalizeFirstWord(name)
}