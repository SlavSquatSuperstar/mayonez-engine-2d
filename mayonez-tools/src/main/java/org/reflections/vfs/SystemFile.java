package org.reflections.vfs;

import java.io.*;

/**
 * A file on the computer's local file system, represented as a {@link java.io.File}.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
class SystemFile implements VfsFile {

    private final SystemDir root;
    private final File file;

    public SystemFile(final SystemDir root, File file) {
        this.root = root;
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public String getRelativePath() {
        String filepath = file.getPath().replace("\\", "/");
        if (filepath.startsWith(root.getPath())) {
            return filepath.substring(root.getPath().length() + 1);
        }
        return null; //should not get here
    }

    @Override
    public String toString() {
        return file.toString();
    }

}
