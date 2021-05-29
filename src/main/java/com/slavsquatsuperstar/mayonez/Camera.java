package com.slavsquatsuperstar.mayonez;

import com.slavsquatsuperstar.mayonez.components.Sprite;
import com.slavsquatsuperstar.util.Constants;

public class Camera {

    private Vector2 position;
    private int width, height;
    private int minX, minY, maxX, maxY;
    private Sprite subject;

    public Camera(Vector2 position, int sceneWidth, int sceneHeight) {
        this.position = position;
        width = Constants.SCREEN_WIDTH;
        height = Constants.SCREEN_HEIGHT;
        minX = 0;
        minY = 0;//-28; // account for the bar on top of the window
        maxX = sceneWidth - width;
        maxY = sceneHeight - height;
    }

    public void move(Vector2 displacement) {
        position = position.add(displacement);
    }

    void update() {
        // Center on the center of the subject
        if (subject == null)
            return;

        // Camera Position = Subject Center - Camera Center
        // Camera X = Subject X + Subject Width / 2 - Camera Width / 2
        // Camera Y = Subject Y + Subject Height / 2 - Camera Height / 2
        float offX = (subject.parent.getX() + subject.getImage().getWidth() / 2) - width / 2;
        float offY = (subject.parent.getY() + subject.getImage().getHeight() / 2) - height / 2;

        position = new Vector2(offX, offY);
        // will this cause a memory leak?
        // TODO set params vs assign new
//			Logger.log("Subject: %.2f, %.2f", subject.getX(), subject.getY());

        // Keep the camera inside the scene
//		position.x = MathUtil.clamp(position.x, minX, maxX);
//		position.y = MathUtil.clamp(position.y, minY, maxY);

        if (position.x < minX)
            position.x = minX;
        else if (position.x > maxX)
            position.x = maxX;

        if (position.y < minY)
            position.y = minY;
        else if (position.y > maxY)
            position.y = maxY;

//		Logger.log("Camera: %.2f, %.2f", position.x, position.y);

    }

    // Getters and Setters

    public void setSubject(Sprite subject) {
        this.subject = subject;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

}
