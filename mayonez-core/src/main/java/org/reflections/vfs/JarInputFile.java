package org.reflections.vfs;

import java.util.zip.*;

/**
 * A file inside a .jar archive, represented as a {@link java.util.jar.JarInputStream}.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
class JarInputFile implements VfsFile {

    private final ZipEntry entry;

    public JarInputFile(ZipEntry entry) {
        this.entry = entry;
    }

    public String getName() {
        String name = entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }

    public String getRelativePath() {
        return entry.getName();
    }

}
