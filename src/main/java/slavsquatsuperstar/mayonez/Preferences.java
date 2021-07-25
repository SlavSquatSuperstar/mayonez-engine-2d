package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.assets.JSONFile;

/**
 * A storage class for game parameters and statistics.
 *
 * @author SlavSquatSuperstar
 */
public final class Preferences { // TODO defaults

    private final static JSONFile preferences = new JSONFile("preferences.json", true);

    private Preferences() {}

    // Window
    public static final String TITLE = preferences.getStr("title");
    public static final String VERSION = preferences.getStr("version");
    public static final int SCREEN_WIDTH = preferences.getInt("width");
    public static final int SCREEN_HEIGHT = preferences.getInt("height");

    // Game
    public static final int FPS = preferences.getInt("fps");;

    // Scene
    public static final int GROUND_HEIGHT = SCREEN_HEIGHT - 30;

    // Physics
    public static final Vector2 GRAVITY = new Vector2(0, 60f);

    // File I/O
    public static final String CHARSET = "UTF-8";

}
