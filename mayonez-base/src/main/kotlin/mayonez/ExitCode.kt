package mayonez

/**
 * An integer value that indicates the termination status of a program.
 * This enum contains reserved exit codes.
 *
 * @author SlavSquatSuperstar
 */
enum class ExitCode(val code: Int) {
    /** The program has successfully completed (0). */
    SUCCESS(0),

    /** The program has terminated due to some error (1). */
    ERROR(1)
}
