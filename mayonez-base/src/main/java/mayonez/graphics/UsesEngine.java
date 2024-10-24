package mayonez.graphics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates which engine type (GL or AWT) a given feature supports.
 *
 * @author SlavsSquatSuperstar
 */
@Target(ElementType.TYPE)
public @interface UsesEngine {
    /**
     * Which engine framework a component is designed to work for.
     */
    EngineType value();
}
