package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.mayonez.components.Sprite;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public final class Assets {

    public static final String ASSETS_DIRECTORY = "assets/";

    static HashMap<String, Sprite> sprites = new HashMap<>();

    private Assets() {}

    /**
     * Accesses a file inside the main resources root.
     *
     * @param filename The location of the file inside "src/main/resources" or the .jar file.
     * @return The file's URL
     */
    public static URL getFile(String filename) {
        URL url = Assets.class.getClassLoader().getResource(filename);
        Logger.log(url.getFile());
        return Objects.requireNonNull(url);
    }

    public static boolean hasSprite(String filename) {
        return sprites.containsKey(filename);
    }

    /**
     * Creates a new sprite and stores it for future use.
     *
     * @param filename The location of the sprite inside "assets/".
     */
    public static void addSprite(String filename) {
        if (!hasSprite(filename)) {
            sprites.put(filename, new Sprite(filename));
            Logger.log("Assets: Created sprite \"%s\"", filename);
        } else {
            Logger.log("Assets: Sprite \"%s\" already exists", filename);
        }
    }

    /**
     * Accesses a sprite stored in memory or creates it if it does not exist.
     *
     * @param filename The location of the sprite inside "assets/"
     */
    public static Sprite getSprite(String filename) {
        filename = Assets.ASSETS_DIRECTORY + filename;
        if (!hasSprite(filename))
            addSprite(filename);
        return sprites.get(filename);
    }

}
