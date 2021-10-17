package slavsquatsuperstar;

import slavsquatsuperstar.mayonez.Game;
import slavsquatsuperstar.sandbox.physics.PhysicsTestScene;

public class Main {

    public static void main(String[] args) {
        Game game = Game.instance();
        game.start();
        Game.loadScene(new PhysicsTestScene("Physics Test Scene"));
    }

}
