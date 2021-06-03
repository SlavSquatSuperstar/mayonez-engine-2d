package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.util.JSONFile;

public final class Preferences {

    private static JSONFile json = new JSONFile("assets/preferences.json");;

    private Preferences() {}

    // Window
    public static final String SCREEN_TITLE = json.getStr("title");
    public static final String VERSION = json.getStr("version");
    public static final int SCREEN_WIDTH = json.getInt("width");
    public static final int SCREEN_HEIGHT = json.getInt("height");

    // Game
    public static final int FPS = 60;

    // Level
    public static final int PLAYER_WIDTH = 42;
    public static final int PLAYER_HEIGHT = 42;
    public static final int GROUND_HEIGHT = SCREEN_HEIGHT - 30;

    // Physics
    public static final Vector2 GRAVITY = new Vector2(0, 60f);
    public static final float TERMINAL_VELOCITY = GRAVITY.y * 2;

}
