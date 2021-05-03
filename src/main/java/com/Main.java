package com;
import com.mayonez.Game;

public class Main {

	public static void main(String[] args) {
		Game game = Game.getGame();
		game.changeScene(0);
		game.start();
	}

}
