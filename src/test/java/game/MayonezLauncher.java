package game;

import io.github.slavsquatsuperstar.mayonez.engine.GameController;
import io.github.slavsquatsuperstar.mayonez.levels.Level;

public class MayonezLauncher {

	public static void main(String[] args) {
		int width = 1280, height = 720;
		
		GameController game = new GameController("Mayonez Engine", width, height);
		game.loadLevel(new Level(width, height));
		game.start();
	}

}
