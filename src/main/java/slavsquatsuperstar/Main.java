package slavsquatsuperstar;

import slavsquatsuperstar.mayonez.Game;

public class Main {

    public static void main(String[] args) {
        Game game = Game.instance();
        game.loadScene(0);
        game.start();
    }

}
