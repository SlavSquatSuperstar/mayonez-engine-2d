package slavsquatsuperstar.mayonez.physics.detection

import slavsquatsuperstar.math.Vec2

import slavsquatsuperstar.util.Pair

/**
 * Describes how much two shapes are overlapping and in what direction.
 *
 * @author SlavSquatSuperstar
 */
class Penetration(val normal: Vec2, val depth: Float) : Pair<Vec2, Float>(normal, depth) {
}