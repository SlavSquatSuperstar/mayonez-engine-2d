package org.reflections.vfs;

import mayonez.*;

import java.net.URL;

/**
 * A bridge between different file types and systems. Removed all methods except for
 * {@link Vfs#fromURL} from org.reflections.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
public final class Vfs {

    private Vfs() {
    }

    /**
     * tries to create a Dir from the given url, using the defaultUrlTypes
     */
    public static VfsDir fromURL(final URL url) {
        for (UrlType type : UrlType.values()) {
            try {
                if (type.matches(url)) {
                    VfsDir dir = type.createDir(url);
                    if (dir != null) return dir;
                }
            } catch (Throwable e) {
                Logger.warn("Could not create Dir using %s from url %s. Skipping".formatted(type, url.toExternalForm()));
                Logger.printStackTrace(e);
            }
        }

        String msg = "Could not create VfsDir from URL \"%s\", no matching UrlType was found."
                .formatted(url.toExternalForm());
        throw new RuntimeException(msg);
    }

}
