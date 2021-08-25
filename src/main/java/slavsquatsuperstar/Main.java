package slavsquatsuperstar;

import slavsquatsuperstar.sandbox.GLTestScene;
import slavsquatsuperstar.mayonezgl.GameGL;

public class Main {

    public static void main(String[] args) {
        GameGL game = GameGL.instance();
        GameGL.setScene(new GLTestScene());
        game.start();
    }

}
