package slavsquatsuperstar.mayonez.graphics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates which engine type (AWT or GL) this a given feature supports.
 */
@Target(ElementType.TYPE)
public @interface Engine {
    /**
     * Which engine framework a component is designed to work for.
     */
    EngineType value();
}
