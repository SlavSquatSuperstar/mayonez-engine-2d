package org.reflections.vfs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * A folder on the computer's local file system, represented as a {@link java.io.File}.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
class SystemDir implements VfsDir {
    private final File file;

    public SystemDir(File file) {
        if (file != null && (!file.isDirectory() || !file.canRead())) {
            throw new RuntimeException("Cannot use dir " + file);
        }
        this.file = file;
    }

    public String getPath() {
        return file != null ? file.getPath().replace("\\", "/") : "/NO-SUCH-DIRECTORY/";
    }

    @SuppressWarnings("resource")
    public Iterable<VfsFile> getFiles() throws RuntimeException {
        if (file == null || !file.exists()) return Collections.emptyList();
        return () -> {
            try {
                return Files.walk(file.toPath())
                        .filter(Files::isRegularFile)
                        .map(path -> (VfsFile) new SystemFile(this, path.toFile()))
                        .iterator();
            } catch (IOException e) {
                throw new RuntimeException("Could not get files for " + file, e);
            }
        };
    }
}