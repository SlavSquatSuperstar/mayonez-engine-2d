package tests;

import com.slavsquatsuperstar.mayonez.*;
import com.slavsquatsuperstar.mayonez.physics2d.AlignedBoxCollider2D;
import com.slavsquatsuperstar.mayonez.physics2d.Line2D;
import com.slavsquatsuperstar.mayonez.physics2d.RigidBody2D;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoxTests {

    private static Game game;
    private static AlignedBoxCollider2D aabb;

    @BeforeClass
    public static void getBox() {
        aabb = new AlignedBoxCollider2D(new Vector2(4,4));
        game = Game.instance();
        game.start();
        game.loadScene(new Scene("Box Test Scene", 100, 100) {
            @Override
            protected void init() {
                addObject(new GameObject("Box Test Object", new Vector2(-2, -2)) {
                    @Override
                    protected void init() {
                        addComponent(new RigidBody2D(0, false));
                        addComponent(aabb);
                    }
                });
            }
        });
    }

    @Test
    public void pointIsInAABB() {
        assertTrue(aabb.contains(aabb.min()));
        assertTrue(aabb.contains(aabb.max()));
        assertTrue(aabb.contains(new Vector2()));
    }

    @Test
    public void pointNotInAABB() {
        assertFalse(aabb.contains(new Vector2(3, -3)));
        assertFalse(aabb.contains(new Vector2(-3, 3)));
        assertFalse(aabb.contains(new Vector2(-3, -3)));
        assertFalse(aabb.contains(new Vector2(3, 3)));
    }

    @Test
    public void lineIsInAABB() {
        assertTrue(aabb.intersects(new Line2D(aabb.min(), aabb.max())));
        assertTrue(aabb.intersects(new Line2D(new Vector2(), aabb.min())));
        assertTrue(aabb.intersects(new Line2D(new Vector2(), aabb.max())));
        assertTrue(aabb.intersects(new Line2D(new Vector2(1,2), new Vector2(-1,2))));
        assertTrue(aabb.intersects(new Line2D(new Vector2(1, 1), new Vector2(3, 3))));
        assertTrue(aabb.intersects(new Line2D(new Vector2(2, 2), new Vector2(3, 3))));
        assertTrue(aabb.intersects(new Line2D(new Vector2(2, 2), new Vector2(2, 3))));
        assertTrue(aabb.intersects(new Line2D(new Vector2(2, 2), new Vector2(3, 1))));
        assertTrue(aabb.intersects(new Line2D(new Vector2(-1, -1), new Vector2(-3, -3))));
    }

    @Test
    public void lineNotInAABB() {
        assertFalse(aabb.intersects(new Line2D(new Vector2(3,3),new Vector2(4,4))));
    }

}
