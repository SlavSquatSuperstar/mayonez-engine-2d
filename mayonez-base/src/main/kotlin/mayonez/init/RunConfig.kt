package mayonez.init

import mayonez.annotations.*

/**
 * A set of fixed runtime parameters for the application.
 *
 * @author SlavSquatSuperstar
 */
class RunConfig(
    /** Whether to use the LWJGL engine. */
    val useGL: Boolean,
    /** Whether to save log files. */
    val saveLogs: Boolean
) {

    companion object {
        @JvmField
        val DEFAULT_ENGINE_TYPE = EngineType.GL

        @JvmField
        val DEFAULT_USE_GL: Boolean = true

        @JvmField
        val DEFAULT_SAVE_LOGS = true

        @JvmField
        val DEFAULT_CONFIG = RunConfig(
            DEFAULT_USE_GL, DEFAULT_SAVE_LOGS
        )
    }
}
