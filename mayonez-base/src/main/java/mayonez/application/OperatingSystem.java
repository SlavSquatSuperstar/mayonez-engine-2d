package mayonez.application;

/**
 * An operating system of a computer running Java.
 *
 * @author SlavSquatSuperstar
 */
public enum OperatingSystem {

    /**
     * An unknown or undefined operating system.
     */
    UNKNOWN("Unknown OS"),

    /**
     * The FreeBSD operating system or its derivatives.
     */
    FREE_BSD("FreeBSD"),

    /**
     * The GNU/Linux family of operating systems.
     */
    LINUX("Linux"),

    /**
     * The macOS/OS X operating system.
     */
    MAC_OS("macOS"),

    /**
     * The Microsoft Windows operating system.
     */
    WINDOWS("Windows");

    private static final OperatingSystem current;

    static {
        var osName = System.getProperty("os.name");
        if (osName.contains("bsd")) current = FREE_BSD;
        else if (osName.contains("linux")) current = LINUX;
        else if (osName.contains("mac")) current = MAC_OS;
        else if (osName.contains("windows")) current = WINDOWS;
        else current = UNKNOWN;
    }

    private final String name;

    OperatingSystem(String name) {
        this.name = name;
    }

    /**
     * Get the current operating system of this device running Java.
     *
     * @return the current OS.
     */
    public static OperatingSystem getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return name;
    }

}
