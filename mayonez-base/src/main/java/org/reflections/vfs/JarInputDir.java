package org.reflections.vfs;

import mayonez.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

/**
 * A folder inside a .jar archive, represented as a {@link java.util.jar.JarInputStream}.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
class JarInputDir implements VfsDir {

    // Source: JDK-6916399, see https://bugs.openjdk.org/browse/JDK-6916399
    private static final long BYTE_MINUS_ONE = 0xffffffffL;

    private final URL url;
    private JarInputStream jarInputStream;
    private long cursor;
    private long nextCursor;

    public JarInputDir(URL url) {
        this.url = url;
        cursor = 0L;
        nextCursor = 0;
    }

    public String getPath() {
        return url.getPath();
    }

    public Iterable<VfsFile> getFiles() throws RuntimeException {
        return () -> new Iterator<>() {
            {
                try {
                    jarInputStream = new JarInputStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    throw new RuntimeException("Could not open url connection", e);
                }
            }

            VfsFile entry = null;

            @Override
            public boolean hasNext() {
                return entry != null || (entry = computeNext()) != null;
            }

            @Override
            public VfsFile next() {
                VfsFile next = entry;
                entry = null;
                return next;
            }

            private VfsFile computeNext() throws RuntimeException {
                while (true) {
                    try {
                        ZipEntry entry = jarInputStream.getNextJarEntry();
                        if (entry == null) {
                            return null;
                        }

                        long size = entry.getSize();
                        if (size < 0) size += BYTE_MINUS_ONE;
                        nextCursor += size;
                        if (!entry.isDirectory()) {
                            return new JarInputFile(entry, JarInputDir.this, cursor, nextCursor);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Could not get next zip entry", e);
                    }
                }
            }
        };
    }

    public void close() {
        try {
            if (jarInputStream != null) jarInputStream.close();
        } catch (IOException e) {
            Logger.warn("Could not close InputStream");
            Logger.printStackTrace(e);
        }
    }

    // Getters and Setters

    boolean isCursorBetween(long fromIndex, long endIndex) {
        return (cursor >= fromIndex) && (cursor <= endIndex);
    }

    int read() throws IOException {
        int read = jarInputStream.read();
        cursor += 1;
        return read;
    }

}
