package slavsquatsuperstar;

import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.sandbox.GLTestScene;

public class Main {

    public static void main(String[] args) {
        GameGL.instance();
        GameGL.setScene(new GLTestScene());
        GameGL.start();
    }

}
