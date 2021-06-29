package slavsquatsuperstar.mayonez.components;

import slavsquatsuperstar.mayonez.*;
import slavsquatsuperstar.util.MathUtils;

public class Camera extends Script {

    private float lastMx, lastMy;
    private final int width, height;
    private final int minX, minY, maxX, maxY;
    private GameObject subject;

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
            parent.setX((subject.getX()) - width / 2f);
            parent.setY((subject.getY()) - height / 2f);
        }
        // Keep camera inside scene
        if (scene().isBounded() && parent.keepInScene) {
            parent.setX(MathUtils.clamp(parent.getX(), minX, maxX));
            parent.setY(MathUtils.clamp(parent.getY(), minY, maxY));
        }
    }

    public float getX() {
        return parent.getX();
    }

    public float getY() {
        return parent.getY();
    }

    public void setSubject(GameObject subject) {
        this.subject = subject;
        parent.keepInScene = subject.keepInScene;
    }
}
