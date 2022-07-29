package slavsquatsuperstar.util

import slavsquatsuperstar.mayonez.annotations.ExperimentalFeature
import java.util.*

/**
 * An immutable two-element tuple storing two objects of any class.
 * @param <L> the class of the left (first) object
 * @param <R> the class of the right (second) object
 */
@ExperimentalFeature
class Pair<L, R>(val left: L, val right: R) {

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other is Pair<*, *> -> left == other.left && right == other.right
            else -> false
        }
    }

    override fun hashCode(): Int = Objects.hash(left, right) // 31 * L + R

    override fun toString(): String = "Pair ($left, $right)"
}