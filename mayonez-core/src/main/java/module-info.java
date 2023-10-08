/**
 * The foundational module of Mayonez Engine, which contains all the classes needed to
 * set up and run an application.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.core {
    // JVM
    requires java.desktop;
    requires kotlin.stdlib;

    // Public API
    exports mayonez.annotations;
}