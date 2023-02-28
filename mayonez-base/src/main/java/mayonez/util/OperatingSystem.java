package mayonez.util;

/**
 * An operating system of a computer running Java. Each operating system defines a file separator
 * and a line separator.
 */
public enum OperatingSystem {

    /**
     * The GNU/Linux family of operating systems.
     */
    LINUX("Linux", "/", "\n", true),

    /**
     * The macOS or OS X family of operating systems.
     */
    MACOS("Mac OS", "/", "\n", true),

    /**
     * The Microsoft Windows family of operating systems.
     */
    WINDOWS("Windows", "\\", "\r\n", false),

    /**
     * An unknown or undefined operating system.
     */
    UNKNOWN("Unknown", "/", "\n", true);

    // Fields and Constructors

    private final String name;
    private final String fileSeparator, lineSeparator;
    private final boolean isUnix;

    OperatingSystem(String name, String fileSeparator, String lineSeparator, boolean isUnix) {
        this.name = name;
        this.fileSeparator = fileSeparator;
        this.lineSeparator = lineSeparator;
        this.isUnix = isUnix;
    }

    // Getter Methods

    public String getFileSeparator() {
        return fileSeparator;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public boolean isUnix() {
        return isUnix;
    }

    @Override
    public String toString() {
        return name;
    }

    // Static Methods

    /**
     * Gets the current operating system of this device running Java.
     *
     * @return the current OS.
     */
    public static OperatingSystem getCurrentOS() {
        var osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")) {
            return LINUX;
        } else if (osName.contains("mac")) {
            return MACOS;
        } else if (osName.contains("windows")) {
            return WINDOWS;
        } else {
            return UNKNOWN;
        }
    }

}
