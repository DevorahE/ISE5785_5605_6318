package primitives;

/**
 * Unit tests for primitives.Point class
 * @author Devorah Wajs and Guila Czerniewicz
 */
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link primitives.Point#subtract(Point)}
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking a subtraction operation between two points
         Point  p1 = new Point(1, 2, 3);
        Point  p2 = new Point(2, 4, 6);
        assertEquals(new Vector(1, 2, 3), p2.subtract(p1), "ERROR: subtract() wrong value");

        // =============== Boundary Values Tests ==================
        // TC10:Subtracting a point from itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "ERROR: (point - itself) does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(Vector)}
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the function add
        Point  p1 = new Point(1, 2, 3);
        Vector v1 = new Vector(1, 2, 3);
        assertEquals(new Point(2, 4, 6), p1.add(v1), "ERROR: (point + vector) = other point does not work correctly");


        // =============== Boundary Values Tests ==================
        // TC10:Testing when we get the zero vector
        Vector v1_OPPOSITE = new Vector(-1, -2, -3);
        assertEquals(Point.ZERO, p1.add(v1_OPPOSITE), "ERROR: (point + vector) = center of coordinates does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(Point)}
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the function DistanceSquared
        Point  p1 = new Point(1, 2, 3);
        Point  p2 = new Point(2, 4, 5);
        assertEquals(9, p1.distanceSquared(p2), DELTA, "ERROR: squared distance between points is wrong");
        assertEquals(9, p2.distanceSquared(p1), DELTA, "ERROR: squared distance between points is wrong");

        // =============== Boundary Values Tests ==================
        // TC10:Distance between a point and itself
        assertEquals(0, p1.distanceSquared(p1), DELTA, "ERROR: point squared distance to itself is not zero");
    }

    /**
     * Test method for {@link primitives.Point#distance(Point)}
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the function Distance
        Point  p1 = new Point(1, 2, 3);
        Point  p2 = new Point(2, 4, 5);
        assertEquals(3, p1.distance(p2), DELTA, "ERROR: distance between points to itself is wrong");


        // =============== Boundary Values Tests ==================
        // TC10:Distance between a point and itself
        assertEquals(0, p1.distance(p1), DELTA, "ERROR: point distance to itself is not zero");
    }
}