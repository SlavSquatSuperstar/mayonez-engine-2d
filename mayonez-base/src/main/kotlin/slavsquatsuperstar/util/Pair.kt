package slavsquatsuperstar.util

import java.util.*

/**
 * An immutable two-element tuple storing two objects of any class.
 * @param <L> the class of the left (first) object
 * @param <R> the class of the right (second) object
 */
class Pair<L, R>(val left: L, val right: R) {

    operator fun contains(obj: Any?): Boolean = obj == left || obj == right

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other is Pair<*, *> -> (this.left == other.left) && (this.right == other.right)
            else -> false
        }
    }

    override fun hashCode(): Int = Objects.hash(left, right) // 31 * L + R

    override fun toString(): String = "Pair ($left, $right)"
}