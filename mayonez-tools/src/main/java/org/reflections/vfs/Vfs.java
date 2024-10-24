package org.reflections.vfs;

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
    public static VfsDir fromURL(final URL url) throws RuntimeException {
        for (UrlType type : UrlType.values()) {
            try {
                if (type.matches(url)) {
                    VfsDir dir = type.createDir(url);
                    if (dir != null) return dir;
                }
            } catch (Throwable e) {
                throw new RuntimeException(
                        "Could not create %s directory from URL \"%s\"; skipping."
                                .formatted(type, url.toExternalForm())
                );
            }
        }

        String msg = "Could not create directory from URL \"%s\"; no matching UrlType was found."
                .formatted(url.toExternalForm());
        throw new RuntimeException(msg);
    }

}
