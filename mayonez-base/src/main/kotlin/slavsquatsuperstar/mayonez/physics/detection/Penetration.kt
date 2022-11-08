package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.mayonez.math.Vec2

/**
 * Describes how much two shapes are overlapping and in what direction.
 *
 * @author SlavSquatSuperstar
 */
class Penetration(val normal: Vec2, val depth: Float)