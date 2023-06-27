package slavsquatsuperstar.demos.geometrydash.components;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;

import java.awt.*;

/**
 * Places tiles in the world on grid spaces.
 *
 * @author SlavSquatSuperstar
 */
// TODO don't place blocks if already exists
// TODO don't place blocks when clicking button
@ExperimentalFeature
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
                getScene().addObject(new GameObject("Placed Block", mousePos) {
                    @Override
                    protected void init() {
//                        Logger.log("cursor: %s", cursor);
                        addComponent(SpritesFactory.createSprite(cursor));
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
