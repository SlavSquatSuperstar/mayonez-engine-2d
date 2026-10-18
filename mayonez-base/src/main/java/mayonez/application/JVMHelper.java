package mayonez.application;

/**
 * Assists in checking JVM environment variables.
 *
 * @author SlavSquatSuperstar
 */
final class JVMHelper {

    private JVMHelper() {
    }

    /**
     * Detect if the current operating system is macOS.
     *
     * @return if running on macOS
     */
    static boolean isMacOS() {
        return OperatingSystem.getCurrent() == OperatingSystem.MAC_OS;
    }

    /**
     * Detect if the <code>-XstartOnFirstThread</code> option was passed for this JVM instance (applies to macOS only).
     * <p>
     * Sources:
     * <ul>
     *     <li><a href="https://jvm-gaming.org/t/starting-jvm-on-mac-with-xstartonfirstthread-programmatically/57547">
     *         JVM Gaming - Starting JVM on first thread</a></li>
     *     <li><a href="https://stackoverflow.com/questions/35842/how-can-a-java-program-get-its-own-process-id">
     *         StackOverflow - Get process ID</a></li>
     * </ul>
     *
     * @return if -XstartOnFirstThread is enabled
     */
    static boolean isStartedOnFirstThread() {
        var pid = ProcessHandle.current().pid(); // Get current process ID

        // Check if environment variable for XstartOnFirstThread is enabled
        var envVar = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);
        return "1".equals(envVar);
    }

}
