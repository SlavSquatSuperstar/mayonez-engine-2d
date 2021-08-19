package slavsquatsuperstar.mayonez;

import slavsquatsuperstar.mayonez.renderer.Renderable;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

public interface IGameWindow {

    void start();
    void stop();
    void render(Renderable r);
    void addKeyInput(KeyAdapter keys);
    void addMouseInput(MouseAdapter mouse);

}
