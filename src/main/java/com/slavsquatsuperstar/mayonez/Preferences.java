package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.mayonez.assets.JSONFile;

public final class Preferences {

    private final static JSONFile preferences = new JSONFile("preferences.json", true);

    private Preferences() {}

    // Window
    public static final String TITLE = preferences.getStr("title");
    public static final String VERSION = preferences.getStr("version");
    public static final int SCREEN_WIDTH = preferences.getInt("width");
    public static final int SCREEN_HEIGHT = preferences.getInt("height");

    // Game
    public static final int FPS = 60;

    // Level
    public static final int TILE_SIZE = preferences.getInt("tile_size");
    public static final int GROUND_HEIGHT = SCREEN_HEIGHT - 30;

    // Physics
    public static final Vector2 GRAVITY = new Vector2(0, 60f);
    public static final float TERMINAL_VELOCITY = GRAVITY.y * 2;

    // File I/O
    public static final String CHARSET = "UTF-8";

}
