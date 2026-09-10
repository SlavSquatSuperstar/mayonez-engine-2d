package mayonez.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates which engine backend a given feature supports. Classes that work
 * for all backends do not need this annotation.
 *
 * @author SlavsSquatSuperstar
 */
@Target(ElementType.TYPE)
public @interface UsesBackend {
    /**
     * Which engine backends a component is designed to work for.
     */
    Backend[] value();
}
