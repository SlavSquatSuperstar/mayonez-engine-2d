package org.reflections.vfs;

import java.io.IOException;
import java.io.InputStream;

/**
 * A file on an arbitrary file system. Originally an inner class, {@code Vfs.File},
 * in org.reflections.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
public interface VfsFile {
    String getName();

    String getRelativePath();

    InputStream openInputStream() throws IOException;
}
