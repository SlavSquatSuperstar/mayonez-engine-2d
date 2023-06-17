package mayonez.physics.detection

import mayonez.math.*

/**
 * Describes how far and in which direction two colliding shapes are
 * overlapping.
 *
 * @author SlavSquatSuperstar
 */
data class Penetration(internal val normal: Vec2, internal val depth: Float) {
    override fun toString(): String = "Penetration $normal, $depth"
}