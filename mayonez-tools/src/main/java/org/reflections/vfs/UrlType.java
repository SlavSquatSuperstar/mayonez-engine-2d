package org.reflections.vfs;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Matches and creates {@link VfsFile} objects from URLs. Originally two separate
 * classes, interface {@code UrlType} and enum {@code DefaultUrlTypes}, in org.reflections.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
enum UrlType {

    /**
     * A directory (folder) on the local computer system.
     */
    DIRECTORY {
        public boolean matches(URL url) {
            if (url.getProtocol().equals("file") && !hasJarFileInPath(url)) {
                File file = getFile(url);
                return file != null && file.isDirectory();
            } else return false;
        }

        public VfsDir createDir(final URL url) {
            return new SystemDir(getFile(url));
        }
    },

    /**
     * Reads .jar contents using an input stream.
     */
    JAR_INPUT_STREAM {
        public boolean matches(URL url) {
            return url.toExternalForm().contains(".jar");
        }

        public VfsDir createDir(final URL url) {
            return new JarInputDir(url);
        }
    };

    public abstract boolean matches(URL url);

    public abstract VfsDir createDir(URL url);

    /**
     * Try to get {@link java.io.File} from url.
     */
    private static File getFile(URL url) {
        File file;
        String path;

        try {
            path = url.toURI().getSchemeSpecificPart();
            if ((file = new File(path)).exists()) return file;
        } catch (URISyntaxException ignored) {
        }

        path = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
        if (path.contains(".jar!")) path = path.substring(0, path.lastIndexOf(".jar!") + ".jar".length());
        if ((file = new File(path)).exists()) return file;

        try {
            path = url.toExternalForm();
            if (path.startsWith("jar:")) path = path.substring("jar:".length());
            if (path.startsWith("wsjar:")) path = path.substring("wsjar:".length());
            if (path.startsWith("file:")) path = path.substring("file:".length());
            if (path.contains(".jar!")) path = path.substring(0, path.indexOf(".jar!") + ".jar".length());
            if (path.contains(".war!")) path = path.substring(0, path.indexOf(".war!") + ".war".length());
            if ((file = new File(path)).exists()) return file;

            path = path.replace("%20", " ");
            if ((file = new File(path)).exists()) return file;

        } catch (Exception ignored) {
        }

        return null;
    }

    private static boolean hasJarFileInPath(URL url) {
        return url.toExternalForm().matches(".*\\.jar(!.*|$)");
    }

}
