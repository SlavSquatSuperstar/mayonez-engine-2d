package slavsquatsuperstar.demos.geometrydash.components;

import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.graphics.Sprite;
import slavsquatsuperstar.mayonez.input.MouseInput;
import slavsquatsuperstar.mayonez.physics.Rigidbody;
import slavsquatsuperstar.mayonez.physics.colliders.BoxCollider;
import slavsquatsuperstar.mayonez.scripts.Counter;

import java.awt.*;

public class SnapToGrid extends Component {

    private Sprite cursor;
    private Counter counter;

    //    private Vec2 gridSize;
    private float placeDelay = 0.2f;

    public SnapToGrid(Vec2 gridSize) {
//        this.gridSize = gridSize;
    }

    @Override
    public void start() {
        counter = new Counter(0f, placeDelay, true).setInitialValue(0);
        counter.start();
        cursor = parent.getComponent(Sprite.class);
        if (cursor == null) setEnabled(false);
    }

    @Override
    public void update(float dt) {
        counter.update(dt);

        // add 0.5 to x/y to center the block
        Vec2 mousePos = MouseInput.getPosition().add(getScene().getCamera().getOffset()).floor().add(new Vec2(0.5f));
        transform.position.set(mousePos);
        if (counter.isReady() && MouseInput.buttonDown("left mouse")) {
            counter.reset();
            // shouldn't add if object already exists
            getScene().addObject(new GameObject("Placed Block", mousePos) {
                @Override
                protected void init() {
                    addComponent(cursor.copy());
                    addComponent(new BoxCollider(new Vec2(1f)));
                    addComponent(new Rigidbody(0f).setFixedRotation(true));
                }
            });
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // make transparent
        cursor.render(g2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // reset alpha
    }
}
