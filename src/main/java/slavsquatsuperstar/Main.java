package slavsquatsuperstar;

import slavsquatsuperstar.mayonezgl.GameGL;
import slavsquatsuperstar.sandbox.GLRendererTest;

public class Main {

    public static void main(String[] args) {
        GameGL.instance();
        GameGL.setScene(new GLRendererTest());
        GameGL.start();
    }

}
