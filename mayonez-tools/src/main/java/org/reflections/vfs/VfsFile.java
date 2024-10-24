package org.reflections.vfs;

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
}
