package slavsquatsuperstar.mayonez.graphics.renderer;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Scene;
import slavsquatsuperstar.mayonez.annotations.EngineType;
import slavsquatsuperstar.mayonez.annotations.UsesEngine;

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
        objects.clear();
        objects.addAll(newScene.getObjects());
//        sprites.clear();
//        newScene.getObjects().forEach(this::addObject);
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
        if (getCamera() != null) {
            Vec2 camOffset = getCamera().getOffset();
            g2.translate(-camOffset.x, -camOffset.y);
        }
        objects.forEach(o -> o.render(g2));
//        sprites.forEach(s -> s.render(g2));
//        debugDraw.render(g2);

        // Reset the screen's transform to its unmodified state
        g2.setTransform(oldXf);
    }

    @Override
    public void stop() {
        objects.clear();
//        sprites.clear();
    }
}
