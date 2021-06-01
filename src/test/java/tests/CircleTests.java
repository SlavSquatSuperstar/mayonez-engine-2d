package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.slavsquatsuperstar.mayonez.*;
import com.slavsquatsuperstar.mayonez.physics2d.Line2D;

import com.slavsquatsuperstar.mayonez.physics2d.CircleCollider;
import com.slavsquatsuperstar.mayonez.physics2d.RigidBody2D;

public class CircleTests {
    private Game game;
    private CircleCollider c;

    public CircleTests() {
        c = new CircleCollider(2);
        game = Game.instance();
        game.start();
        game.loadScene(new Scene("Circle Test Scene", 100, 100) {
            @Override
            protected void init() {
                addObject(new GameObject("Circle Test Object", new Vector2(0, 0)) {
                    @Override
                    protected void init() {
                        addComponent(new RigidBody2D(0, false));
                        addComponent(c);
                    }
                });
            }
        });
    }

    //    @Test
    public void pointIsInCircle() {
        assertTrue(c.contains(new Vector2(0, 2)));
        assertTrue(c.contains(new Vector2(2, 0)));
        assertTrue(c.contains(new Vector2(2, 2)));
        assertTrue(c.contains(new Vector2(2, 4)));
        assertTrue(c.contains(new Vector2(4, 2)));
    }

    //    @Test
    public void pointNotInCircle() {
        assertFalse(c.contains(new Vector2(0, 0)));
        assertFalse(c.contains(new Vector2(4, 4)));
    }

    //    @Test
    public void lineIsInCircle() {
        assertTrue(c.intersects(new Line2D(new Vector2(2, 0), new Vector2(2, 4))));
        assertTrue(c.intersects(new Line2D(new Vector2(0, 2), new Vector2(4, 2))));
        assertTrue(c.intersects(new Line2D(new Vector2(0, 0), new Vector2(4, 4))));
        assertTrue(c.intersects(new Line2D(new Vector2(1, 1), new Vector2(3, 3))));
    }

    //    @Test
    public void lineNotInCircle() {
        assertFalse(c.intersects(new Line2D(new Vector2(0, 5), new Vector2(4, 5))));
    }

}
