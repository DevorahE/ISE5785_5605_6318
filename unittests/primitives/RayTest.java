package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Ray class
 * @author Devorah Wajs and Guila Czerniewicz
 */

class RayTest {

    /**
     * Test method for {@link primitives.Ray#Ray(Point, Vector)}
     */
    @Test
    void testConstructor1() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: The Ray's direction vector is normalized
        Ray ray = new Ray(new Point(1, 2, 3), new Vector(1, 0, 0));
        assertEquals(1, ray.getDirection().length(), "ERROR: the Ray's direction vector isn't normalized ");
    }

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}
     */
    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Point(1, 2, 3), new Vector(1, 0, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: t is a negative number
        assertEquals(new Point(-1,2,3), ray.getPoint(-2), "ERROR: From getPoint: Wrong point with negative t");

        // TC02: t is a positive number
        assertEquals(new Point(3,2,3), ray.getPoint(2) , "ERROR: From getPoint: Wrong point with positive t");

        // =============== Boundary Values Tests =================
        // TC03: t is zero
        assertEquals(new Point(1,2,3), ray.getPoint(0) , "ERROR: From getPoint: Wrong point with t=0");
    }

    /**
     * Test method for
     * {@link primitives.Ray#findClosestPoint(List)}
     */
    @Test
    void testFindClosestPoint() {
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(2, 0, 0);
        Point p3 = new Point(3, 0, 0);
        Vector v = new Vector(1, 0, 0);
        List<Point> list = List.of(p1, p2, p3);

        // ============ Equivalence Partitions Tests ==============
        // EP01: closest point is in the middle of the list
        assertEquals(p2, new Ray(new Point(2.1, 0, 0), v).findClosestPoint(list), "ERROR: Closest point is in the middle of the list");


        // =============== Boundary Values Tests ==================

        // BV01: empty list
        assertNull(new Ray(new Point(2.1, 0, 0), v).findClosestPoint(null), "ERROR: the list is empty, the result must ne Null");

        // BV02:  first point in the list
        assertEquals(p1, new Ray(new Point(1.1, 0, 0), v).findClosestPoint(list), "ERROR: first point in the list");

        // BV03: last point in the list
        assertEquals(p3, new Ray(new Point(3.1, 0, 0), v).findClosestPoint(list), "ERROR: last point in the list");
    }
}