package com.slavsquatsuperstar.mayonez.components;

import com.slavsquatsuperstar.mayonez.Camera;
import com.slavsquatsuperstar.mayonez.Game;
import com.slavsquatsuperstar.mayonez.MouseInput;
import com.slavsquatsuperstar.mayonez.Vector2;

public class CameraController extends Script {

    private float lastMx, lastMy;

    @Override
    public void update(float dt) {
        MouseInput mouse = Game.mouse();
        if (mouse.pressed() && Game.mouse().button() == 3) {
            float dx = mouse.getX() + mouse.getDx() - lastMx;
            float dy = mouse.getY() + mouse.getDy() - lastMy;

            ((Camera) parent).move(new Vector2(-dx, -dy));
        }
        lastMx = mouse.getX() + mouse.getDx();
        lastMy = mouse.getY() + mouse.getDy();
    }
}
