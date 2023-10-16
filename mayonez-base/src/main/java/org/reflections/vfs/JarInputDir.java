package org.reflections.vfs;

import mayonez.util.*;

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

    private final URL url;
    private JarInputStream jarInputStream;

    public JarInputDir(URL url) {
        this.url = url;
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
                        if (!entry.isDirectory()) {
                            return new JarInputFile(entry);
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

}
