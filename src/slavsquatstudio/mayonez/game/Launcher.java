package slavsquatstudio.mayonez.game;

import org.json.simple.JSONObject;

import slavsquatstudio.lib.io.JSONFileUtil;
import slavsquatstudio.mayonez.engine.Engine;

/**
 * The entry point for the engine.
 */
public class Launcher {

	private static final JSONObject PREFERENCES = JSONFileUtil.parseJSONFile("./preferences.json");

	// Title
	public static final String NAME = JSONFileUtil.getString("name", PREFERENCES);
	public static final String VERSION = JSONFileUtil.getString("version", PREFERENCES);

	// Dimensions
	public static final int WIDTH = JSONFileUtil.getInt("width", PREFERENCES);
	public static final int HEIGHT = JSONFileUtil.getInt("height", PREFERENCES);

	public static void main(String[] args) {
		Engine engine = new Engine();
		engine.start();
	}

}
