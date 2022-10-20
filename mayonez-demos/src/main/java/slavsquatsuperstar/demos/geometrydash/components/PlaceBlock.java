package slavsquatsuperstar.demos.geometrydash.components;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.graphics.JSprite;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.io.JTexture;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.Counter;

import java.awt.*;

// TODO don't place duplicate blocks
// TODO don't place blocks when selecting button
public class PlaceBlock extends Component {

    private JTexture cursor;
    private Counter counter;

    private final float placeDelay = 0.2f;

    @Override
    public void start() {
        counter = new Counter(0f, placeDelay, true).setInitialValue(0);
        counter.start();
    }

    @Override
    public void update(float dt) {
        counter.update(dt);

        if (cursor == null) return;
        // add 0.5 to x/y to center the block
        Vec2 mousePos = MouseInput.getPosition().add(new Vec2(0.5f)).floor();
        transform.position.set(mousePos);
        if (counter.isReady() && MouseInput.buttonDown("left mouse")) {
            counter.reset();
            // shouldn't add if object already exists
            getScene().addObject(new GameObject("Placed Block", mousePos) {
                @Override
                protected void init() {
                    addComponent(new JSprite(cursor));
                    addComponent(new BoxCollider(new Vec2(1f)));
                    addComponent(new Rigidbody(0f).setFixedRotation(true));
                }
            });
        }
    }

    @Override
    public void render(Graphics2D g2) {
        if (cursor == null) return;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // make transparent
        cursor.draw(g2, this.transform, new Transform(), getScene().getCellSize());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // reset alpha
    }

    public void setCursor(JTexture cursor) {
        this.cursor = cursor;
    }

}
