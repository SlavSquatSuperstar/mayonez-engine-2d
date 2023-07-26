package org.reflections.vfs;

/**
 * A directory (folder) on an arbitrary file system. Originally an inner class,
 * {@code Vfs.Dir}, in org.reflections.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
public interface VfsDir extends AutoCloseable {
    String getPath();

    Iterable<VfsFile> getFiles();

    default void close() {
    }

}
