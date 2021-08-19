package slavsquatsuperstar;

import slavsquatsuperstar.mayonezgl.GameGL;

public class Main {

    public static void main(String[] args) {
//        Game game = Game.instance();
//        game.start();
//        Game.loadScene(2);

        new GameGL("Mayonez + LWJGL", 300, 300).start();
    }

}
