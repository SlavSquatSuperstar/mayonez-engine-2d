package mayonez.init

/**
 * A set of fixed runtime parameters for the application.
 *
 * @author SlavSquatSuperstar
 */
@JvmRecord
data class RunConfig(
    /** Whether to use the LWJGL engine. */
    internal val useGL: Boolean,
) {
    companion object {
        const val DEFAULT_USE_GL: Boolean = true
    }
}
