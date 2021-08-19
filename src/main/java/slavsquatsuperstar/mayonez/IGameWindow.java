package slavsquatsuperstar.mayonez;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

public interface IGameWindow {

    void start();
    void stop();
    void addKeyInput(KeyAdapter keys);
    void addMouseInput(MouseAdapter mouse);

}
