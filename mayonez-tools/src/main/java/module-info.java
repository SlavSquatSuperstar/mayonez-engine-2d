/**
 * A library containing basic tools for math and file I/O that may be reused elsewhere.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.tools {
    // JVM
    requires kotlin.stdlib;

    // Other Dependencies
    requires org.json;

    // Shared API
    exports mayonez.annotations;
    exports mayonez.math;
    exports mayonez.math.shapes;
    exports mayonez.util;

    // File IO
    exports mayonez.io;
    exports mayonez.io.image;
    exports mayonez.io.scanner;
    exports mayonez.io.text;
}