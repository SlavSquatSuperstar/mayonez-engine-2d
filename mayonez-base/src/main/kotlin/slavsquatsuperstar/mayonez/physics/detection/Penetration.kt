package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.Vec2
import slavsquatsuperstar.mayonez.physics.shapes.Shape

/**
 * Describes how much two shapes are overlapping and in what direction.
 *
 * @author SlavSquatSuperstar
 */
class Penetration(
    internal val shape1: Shape, internal val shape2: Shape,
    internal val normal: Vec2, internal val depth: Float
) {
}