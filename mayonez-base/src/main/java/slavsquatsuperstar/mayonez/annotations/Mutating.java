package slavsquatsuperstar.mayonez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that a method changes the internal state or data of an object that usually immutable.
 *
 * @author SlavSquatSuperstar
 */
@Target(ElementType.METHOD)
public @interface Mutating {
}
