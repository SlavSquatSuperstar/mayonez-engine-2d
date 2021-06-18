package com.slavsquatsuperstar.mayonez.components;

import com.slavsquatsuperstar.mayonez.*;
import com.slavsquatsuperstar.mayonez.components.Script;
import com.slavsquatsuperstar.mayonez.components.Sprite;
import com.slavsquatsuperstar.util.MathUtil;

public class Camera extends Script {

    private float lastMx, lastMy;
    private final int width, height;
    private final int minX, minY, maxX, maxY;
    private Sprite subject;

    public Camera(int sceneWidth, int sceneHeight) {
        width = Preferences.SCREEN_WIDTH;
        height = Preferences.SCREEN_HEIGHT;
        minX = 0;
        minY = 0;//-28; // account for the bar on top of the window
        maxX = sceneWidth - width;
        maxY = sceneHeight - height;
    }

    @Override
    public void update(float dt) {
        MouseInput mouse = Game.mouse();
        if (mouse.pressed() && Game.mouse().button() == 3) {
            if (mouse.clicks() == 2) { // Reset camera position with double click
                parent.setX(0);
                parent.setY(0);
            } else {
                float dx = mouse.getX() + mouse.getDx() - lastMx;
                float dy = mouse.getY() + mouse.getDy() - lastMy;
                parent.transform.move(new Vector2(-dx, -dy));
            }
        }
        lastMx = mouse.getX() + mouse.getDx();
        lastMy = mouse.getY() + mouse.getDy();

        // Follow subject (Set camera's center equal to subject's center)
        if (subject != null) {
            parent.setX((subject.parent.getX() + subject.getImage().getWidth() / 2) - width / 2);
            parent.setY((subject.parent.getY() + subject.getImage().getHeight() / 2) - height / 2);
        }
        // Keep camera inside scene
        if (scene().isBounded() && parent.shouldKeepInScene()) {
            parent.setX(MathUtil.clamp(parent.getX(), minX, maxX));
            parent.setY(MathUtil.clamp(parent.getY(), minY, maxY));
        }
    }

    public float getX() {
        return parent.getX();
    }

    public float getY() {
        return parent.getY();
    }

    public void setSubject(GameObject subject) {
        this.subject = subject.getComponent(Sprite.class);
    }
}
