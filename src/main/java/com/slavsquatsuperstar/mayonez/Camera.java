package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.mayonez.components.CameraController;
import com.slavsquatsuperstar.mayonez.components.Sprite;
import com.slavsquatsuperstar.util.MathUtil;

public class Camera extends GameObject {

    private int width, height;
    private int minX, minY, maxX, maxY;
    private Sprite subject;

    public Camera(Vector2 position, int sceneWidth, int sceneHeight) {
        super("Camera", position);
        width = Preferences.SCREEN_WIDTH;
        height = Preferences.SCREEN_HEIGHT;
        minX = 0;
        minY = 0;//-28; // account for the bar on top of the window
        maxX = sceneWidth - width;
        maxY = sceneHeight - height;
        keepInScene = false;
    }

    @Override
    protected void init() {
        addComponent(new CameraController());
    }

    public void move(Vector2 displacement) {
        transform.move(displacement);
    }

    @Override
    protected void update(float dt) {
        super.update(dt);
        // Follow subject (Set camera's center equal to subject's center)
        if (subject != null) {
            setX((subject.parent.getX() + subject.getImage().getWidth() / 2) - width / 2);
            setY((subject.parent.getY() + subject.getImage().getHeight() / 2) - height / 2);
        }
        // Keep camera inside scene
        if (scene.isBounded() && keepInScene) {
            setX(MathUtil.clamp(getX(), minX, maxX));
            setY(MathUtil.clamp(getY(), minY, maxY));
        }
    }

    // Getters and Setters
    public void setSubject(Sprite subject) {
        this.subject = subject;
    }

    // Don't want to get rid of the camera!
    @Override
    public void destroy() {
        return;
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}
