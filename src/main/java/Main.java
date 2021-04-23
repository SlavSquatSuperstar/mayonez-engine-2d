import game.TestScene;
import mayonez.Game;

public class Main {
	
	public static void main(String[] args) {
		Game game = Game.instance();
		game.loadScene(new TestScene("Test Scene", 1080, 720));
		game.start();
	}

}
