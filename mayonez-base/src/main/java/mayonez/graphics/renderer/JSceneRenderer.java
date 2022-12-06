package mayonez.graphics.renderer;

import mayonez.Transform;
import mayonez.graphics.Camera;
import mayonez.math.Vec2;
import mayonez.GameObject;
import mayonez.Scene;
import mayonez.annotations.EngineType;
import mayonez.annotations.UsesEngine;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Draws all in-game textures and shapes onto the screen with Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
public final class JSceneRenderer implements SceneRenderer {

    private final List<GameObject> objects;
    private boolean sort = false; // Re-sort sprites this frame
//    private final List<JSprite> sprites; // if use sprites, then no custom render

    public JSceneRenderer() {
        objects = new ArrayList<>();
//        sprites = new ArrayList<>();
    }

    // GameObject Methods

    @Override
    public void setScene(Scene newScene) {
        objects.addAll(newScene.getObjects());
        sort = true;
//        newScene.getObjects().forEach(this::addObject);
    }

    @Override
    public void start() {
        objects.clear();
//        sprites.clear();
    }

    @Override
    public void addObject(GameObject obj) {
        objects.add(obj);
        sort = true;
//        JSprite sprite = obj.getComponent(JSprite.class);
//        if (sprite != null) {
//            sprites.add(sprite);
//            sort = true;
//        }
    }

    @Override
    public void removeObject(GameObject obj) {
        objects.remove(obj);
//        JSprite sprite = obj.getComponent(JSprite.class);
//        if (sprite != null) sprites.remove(sprite);
    }

    // Renderer Methods

    @Override
    public void render(Graphics2D g2) {
        if (g2 == null) return;

        if (sort) {
            objects.sort(Comparator.comparingInt(GameObject::getZIndex));
//            sprites.sort(Comparator.comparingInt(s -> s.getObject().getZIndex()));
            sort = false;
        }

        // Save a copy of the unmodified transform
        AffineTransform oldXf = g2.getTransform();

        // Move the screen and render everything at the offset position
        Camera cam = getCamera();
        if (cam != null) {
            Transform camXf = cam.getTransform();
            Vec2 camOffset = cam.getOffset();
            float camRot = cam.getTransform().rotation;
            Vec2 camScl = cam.getTransform().scale;
            g2.translate(-camOffset.x, -camOffset.y);
//            g2.rotate(Math.toRadians(-cam.getTransform().rotation));
//            g2.scale(camScl.x, camScl.y);
//            g2.setColor(Color.BLACK);
//            g2.fillOval((int) (camOffset.x - 5), (int) (camOffset.y - 5), 10, 10);
        }

        objects.forEach(o -> o.render(g2));
//        sprites.forEach(s -> s.render(g2));

        // Reset the screen's transform to its unmodified state
        g2.setTransform(oldXf);
    }

    @Override
    public void stop() {
        objects.clear();
//        sprites.clear();
    }
}
