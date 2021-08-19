package slavsquatsuperstar;

import slavsquatsuperstar.mayonezgl.GameGL;

public class Main {

    public static void main(String[] args) {
//        Game game = Game.instance();
        GameGL game = GameGL.instance();
        game.start();
//        Game.loadScene(2);
    }

}
