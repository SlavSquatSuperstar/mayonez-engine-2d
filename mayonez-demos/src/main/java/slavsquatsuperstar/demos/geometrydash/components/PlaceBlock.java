package slavsquatsuperstar.demos.geometrydash.components;

import mayonez.GameObject;
import mayonez.Logger;
import mayonez.Script;
import mayonez.Transform;
import mayonez.graphics.JRenderable;
import mayonez.graphics.sprites.Sprite;
import mayonez.input.MouseInput;
import mayonez.graphics.textures.JTexture;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import mayonez.scripts.Timer;

import java.awt.*;

// TODO don't place duplicate blocks
// TODO don't place blocks when selecting button
public class PlaceBlock extends Script implements JRenderable {

    private JTexture cursor;
    private Timer timer;

    @Override
    public void init() {
        timer = new Timer(0.2f);
        gameObject.addComponent(timer);
    }

    @Override
    public void update(float dt) {
        // add 0.5 to x/y to center the block
        var mousePos = MouseInput.getPosition().add(new Vec2(0.5f)).floor();
        transform.setPosition(mousePos);
        // null locks are still being placed
        if (timer.isReady() && MouseInput.buttonDown("left mouse")) {
            timer.reset();
            if (cursor != null) { // add only when selecting
                // TODO shouldn't add if block already exists
                getScene().addObject(new GameObject("Placed Block", mousePos) {
                    @Override
                    protected void init() {
                        Logger.log("cursor: %s", cursor);
                        addComponent(Sprite.create(cursor));
                        addComponent(new BoxCollider(new Vec2(1f)));
                        addComponent(new Rigidbody(0f).setFixedRotation(true));
                    }
                });
            }
        }
    }

    @Override
    public void render(Graphics2D g2) {
        if (cursor == null) return;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // make transparent
        cursor.draw(g2, this.transform, new Transform(), getScene().getScale());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // reset alpha
    }

    public void setCursor(JTexture cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }
}
