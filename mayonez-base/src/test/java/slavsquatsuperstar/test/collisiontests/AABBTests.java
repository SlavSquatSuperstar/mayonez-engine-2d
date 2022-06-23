package slavsquatsuperstar.test.collisiontests;

/**
 * Unit tests for BoundingBoxCollider2D class.
 *
 * @author SlavSquatSuperstar
 */
public class AABBTests {

//    static BoundingBoxCollider2D aabb;
//
//    // Create box centered at (0, 0) with dimensions 2x2 and scale it by 2x
//    @BeforeAll
//    public static void getAABB() {
//        aabb = new BoundingBoxCollider2D(new Vec2(2, 2)).setTransform(new Transform().resize(new Vec2(2, 2)));
//    }
//
//    // Contains Point
//
//    @Test
//    public void vertexPointIsInAABB() {
//        for (Vec2 v : aabb.getVertices())
//            assertTrue(aabb.contains(v));
//    }
//
//    @Test
//    public void edgePointIsInAABB() {
//        assertTrue(aabb.contains(new Vec2(2, 0)));
//        assertTrue(aabb.contains(new Vec2(0, -2)));
//    }
//
//    @Test
//    public void interiorPointIsInAABB() {
//        assertTrue(aabb.contains(new Vec2(1.9f, -1.9f)));
//        assertTrue(aabb.contains(new Vec2(-1, -1)));
//    }
//
//    @Test
//    public void exteriorPointNotInAABB() {
//        assertFalse(aabb.contains(new Vec2(2.1f, -2.1f)));
//        assertFalse(aabb.contains(new Vec2(-3, 3)));
//        assertFalse(aabb.contains(new Vec2(0, 5)));
//    }
//
//    // Nearest Point
//
//    @Test
//    public void nearestPointInsideAABB() {
//        assertEquals(new Vec2(1, 1), aabb.nearestPoint(new Vec2(1, 1)));
//    }
//
//    @Test
//    public void nearestPointOutsideAABB() {
//        assertEquals(new Vec2(2, 2), aabb.nearestPoint(new Vec2(3, 4)));
//    }
//
//    // Raycast
//
//    @Test
//    public void outsideRayHitsAABB() {
//        assertNotNull(aabb.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(1, 0)), 0));
//        assertNotNull(aabb.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(1, -1)), 0));
//    }
//
//    @Test
//    public void outsideRayMissesAABB() {
//        assertNull(aabb.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(-1, 0)), 0));
//        assertNull(aabb.raycast(new Ray2D(new Vec2(-5, 5), new Vec2(-1, 1)), 0));
//    }
//
//    @Test
//    public void limitedOutsideRayHitsAABB() {
//        assertNotNull(aabb.raycast(new Ray2D(new Vec2(-4, 0), new Vec2(1, 0)), 5));
//        assertNotNull(aabb.raycast(new Ray2D(new Vec2(-4, 4), new Vec2(1, -1)), 5));
//    }
//
//    @Test
//    public void limitedOutsideRayMissesAABB() {
//        assertNull(aabb.raycast(new Ray2D(new Vec2(-10, 0), new Vec2(1, 0)), 2));
//        assertNull(aabb.raycast(new Ray2D(new Vec2(-10, 10), new Vec2(1, -1)), 2));
//    }
//
//    @Test
//    public void insideRayHitsAABB() {
//        assertNotNull(aabb.raycast(new Ray2D(new Vec2(-1.5f, 0), new Vec2(1, 0)), 0));
//        assertNotNull(aabb.raycast(new Ray2D(new Vec2(-1.5f, 1.5f), new Vec2(1, -1)), 0));
//    }
//
//    // Line Intersection
//
//    @Test
//    public void interiorLineIsInAABB() {
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(1.5f, 1.5f), new Vec2(-1, -1))));
//    }
//
//    @Test
//    public void tangentLineIsInAABB() {
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 3), new Vec2(3, 1))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-1, -3), new Vec2(-3, -1))));
//    }
//
//    @Test
//    public void bisectLineIsInAABB() {
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-3, -3), new Vec2(3, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, -3), new Vec2(0, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, 0), new Vec2(2, 0))));
//    }
//
//    @Test
//    public void edgeLineIsInAABB() {
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2, -2), new Vec2(-2, 2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 3), new Vec2(2, -3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-2.1f, 2), new Vec2(1.9f, 2))));
//    }
//
//    @Test
//    public void lineIsInAABB() {
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, 0), new Vec2(-2, -2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(0, 0), new Vec2(2, 2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 2), new Vec2(-1, 2))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(1, 1), new Vec2(3, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(3, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(2, 3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(2, 2), new Vec2(3, 1))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(-1, -1), new Vec2(-3, -3))));
//        assertTrue(aabb.intersects(new Edge2D(new Vec2(3, 2), new Vec2(2, 2))));
//    }
//
//    @Test
//    public void lineNotInAABB() {
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(3, 3), new Vec2(4, 4))));
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(4, 4), new Vec2(3, 3))));
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(5, 2), new Vec2(4, 2))));
//        assertFalse(aabb.intersects(new Edge2D(new Vec2(3, 1), new Vec2(3, -1))));
//    }
//
//    // AABB vs Shape
//
//    @Test
//    public void aabbIntersectsCircle() {
//        CircleCollider2D c = new CircleCollider2D(4);
//        c.setTransform(new Transform(new Vec2(2, 2)));
//        assertTrue(aabb.detectCollision(c));
//    }
//
//    @Test
//    public void aabbIntersectsAABB() {
//        BoundingBoxCollider2D other = new BoundingBoxCollider2D(new Vec2(4, 4));
//        other.setTransform(new Transform(new Vec2(2, 2)));
//        assertTrue(aabb.detectCollision(other));
//    }

}
