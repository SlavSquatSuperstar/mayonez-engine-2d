package game;

import java.awt.Dimension;
import java.awt.Toolkit;

import io.github.slavsquatsuperstar.mayonez.engine.GameController;
import io.github.slavsquatsuperstar.mayonez.levels.Level;

public class MayonezLauncher {

	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width, height = screenSize.height;

		GameController game = new GameController("Mayonez Engine", width, height);
		game.loadLevel(new Level(width, height));
		game.start();
	}

}
