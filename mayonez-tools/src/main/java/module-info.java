/**
 * A library containing basic tools for math and file I/O that may be reused elsewhere.
 *
 * @author SlavSquatSuperstar
 */
module mayonez.tools {
    // Dependencies
    requires kotlin.stdlib;
    requires org.json;

    // Shared API
    exports mayonez.annotations;
    exports mayonez.util;

    // File IO
    exports mayonez.io;
    exports mayonez.io.image;
    exports mayonez.io.scanner;
    exports mayonez.io.text;
}