package slavsquatsuperstar.mayonez.physics2d

class PhysicsMaterial(
    /**
     * How much this object resists applied forces and comes to rest while in motion.
     * 0 indicates no opposition, and 1 indicates a very strong opposition.
     */
    val dynamicFriction: Float,

    /**
     * How difficult it is to move this object form a standstill
     * 0 indicates no opposition, and 1 indicates a very strong opposition.
     */
    val staticFriction: Float,

    /**
     * How much kinetic energy is conserved after a collision.
     * 0 means all energy is lost, and 1 means energy is perfectly conserved.
     */
    val bounce: Float
) {
    companion object {
        @JvmField
        val DEFAULT_MATERIAL = PhysicsMaterial(0.5f, 0.5f, 0f)
    }
}