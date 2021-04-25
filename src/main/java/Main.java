import game.PlayerController;
import mayonez.Game;
import mayonez.GameObject;
import mayonez.RectangleCollider;
import mayonez.RigidBody;
import mayonez.Scene;
import mayonez.TexturedSprite;

public class Main {

	public static void main(String[] args) {
		Game game = Game.getGame();
		GameObject player = new GameObject("Player", 100, 100, 64, 64) {
			@Override
			protected void init() {
				addComponent(new TexturedSprite("mario.png"));
				addComponent(new RectangleCollider(getWidth(), getHeight()));
				addComponent(new RigidBody(2));
				addComponent(new PlayerController());
//				scene.getCamera().setSubject(this);
			}
		};
		Scene scene = new Scene("Test Scene", 800, 600) {
			@Override
			protected void init() {
				addObject(player);
			}
		};
		game.loadScene(scene);
		game.start();
	}

}
