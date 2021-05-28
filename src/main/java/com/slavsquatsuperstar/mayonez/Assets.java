package com.slavsquatsuperstar.mayonez;

import java.util.HashMap;

import com.slavsquatsuperstar.mayonez.components.Sprite;

public final class Assets {

	public static final String ASSETS_DIRECTORY = "assets/";

	static HashMap<String, Sprite> sprites = new HashMap<>();

	private Assets() {
	}

	public static boolean hasSprite(String filename) {
		return sprites.containsKey(filename);
	}

	/**
	 * Creates a new sprite and stores it for future use.
	 * 
	 * @param filename The location of the sprite inside "assets/"
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
		if (hasSprite(filename)) {
			return sprites.get(filename);
		} else {
//			Logger.log("Assets: Sprite \"%s\" not found", filename);
			addSprite(filename);
			return sprites.get(filename);
		}
	}

}
