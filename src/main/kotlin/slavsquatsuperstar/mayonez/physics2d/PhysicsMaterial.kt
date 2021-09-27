package slavsquatsuperstar.mayonez.physics2d

class PhysicsMaterial(
    /**
     * How much this object resists applied forces and comes to rest.
     * 0 indicates no opposition, and 1 indicates a very strong opposition.
     */
    val friction: Float,

    /**
     * How much kinetic energy is conserved after a collision.
     * 0 means all energy is lost, and 1 means energy is perfectly conserved.
     */
    val bounce: Float
) {
    companion object {
        @JvmStatic
        val DEFAULT_MATERIAL = PhysicsMaterial(0.5f, 0f)
    }
}