package slavsquatsuperstar.math.geom

import slavsquatsuperstar.math.Vec2

/**
 * A rectangle whose base and height have the same length
 *
 * @param length the side length of the square
 */
class Square(center: Vec2, val length: Float): Rectangle(center, Vec2(length)) {
}