package com.slavsquatsuperstar;
import com.slavsquatsuperstar.mayonez.Game;

public class Main {

	public static void main(String[] args) {
		Game game = Game.instance();
		game.changeScene(0);
		game.start();
	}

}
