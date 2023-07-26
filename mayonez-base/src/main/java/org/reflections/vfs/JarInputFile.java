package org.reflections.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.*;

/**
 * A file inside a .jar archive, represented as a {@link java.util.jar.JarInputStream}.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
class JarInputFile implements VfsFile {

    private final ZipEntry entry;
    private final JarInputDir jarInputDir;
    private final long fromIndex;
    private final long endIndex;

    public JarInputFile(ZipEntry entry, JarInputDir jarInputDir, long cursor, long nextCursor) {
        this.entry = entry;
        this.jarInputDir = jarInputDir;
        fromIndex = cursor;
        endIndex = nextCursor;
    }

    public String getName() {
        String name = entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }

    public String getRelativePath() {
        return entry.getName();
    }

    public InputStream openInputStream() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                if (jarInputDir.isCursorBetween(fromIndex, endIndex)) {
                    return jarInputDir.read();
                } else {
                    return -1;
                }
            }
        };
    }

}
