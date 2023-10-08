package mayonez

/**
 * An integer value that indicates the termination status of a program.
 * This class contains reserved exit codes.
 *
 * @author SlavSquatSuperstar
 */
object ExitCode {
    /** The program has successfully completed (0). */
    const val SUCCESS: Int = 0

    /** The program has terminated due to some error (1). */
    const val ERROR: Int = 1
}
