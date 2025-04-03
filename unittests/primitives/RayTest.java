package primitives;

import org.junit.jupiter.api.Test;

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
}