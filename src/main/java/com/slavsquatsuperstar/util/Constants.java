package com.slavsquatsuperstar.util;

import com.slavsquatsuperstar.mayonez.Vector2;

public final class Constants {

	private Constants() {}

	// Window
	public static final int SCREEN_WIDTH = 1080;
	public static final int SCREEN_HEIGHT = 720;
	public static final String SCREEN_TITLE = "Mayonez Engine";

	// Game
	public static final int FPS = 60;

	// Player
	public static final int PLAYER_WIDTH = 42;
	public static final int PLAYER_HEIGHT = 42;

	// Physics
	public static final Vector2 GRAVITY = new Vector2(0, 50f);
	public static final float TERMINAL_VELOCITY = GRAVITY.y * 5;

}
