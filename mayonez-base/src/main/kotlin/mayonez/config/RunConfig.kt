package mayonez.config

/**
 * A set of fixed runtime parameters for the application.
 *
 * @author SlavSquatSuperstar
 */
@JvmRecord
data class RunConfig(
    /** Whether to use the LWJGL engine. */
    val useGL: Boolean,
    /** Whether to use the old OpenGL version. */
    val glFallback: Boolean,
) {
    companion object {
        const val DEFAULT_USE_GL: Boolean = true
        const val DEFAULT_GL_FALLBACK: Boolean = false
        val DEFAULT_CONFIG: RunConfig = RunConfig(DEFAULT_USE_GL, DEFAULT_GL_FALLBACK)
    }
}
