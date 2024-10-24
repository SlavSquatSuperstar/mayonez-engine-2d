package mayonez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that a given feature is still being developed or tested and is not ready for use in a stable release.
 * Experimental components are expected to contain bugs and other undefined behavior, and may change drastically
 * throughout versions. Can be applied to types (classes), and methods.
 *
 * @author SlavSquatSuperstar
 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ExperimentalFeature {
}
