package geometries;

/**
 * Unit tests for primitives.Vector class
 * @author Devorah Wajs and Guila Czerniewicz
 */

import primitives.Double3;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;


class PlaneTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: Verifies that the normal vector is perpendicular to the vectors between points.
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        assertEquals(0, plane.getNormal().dotProduct(v1), DELTA, "ERROR: Normal is not perpendicular to v1");
        assertEquals(0, plane.getNormal().dotProduct(v2), DELTA, "ERROR: Normal is not perpendicular to v2");

        //TC02: Verifies that the normal vector is a unit vector (length = 1).
        assertEquals(1, plane.getNormal().length(), DELTA, "ERROR: Normal vector is not normalized");

        // =============== Boundary Values Tests ==================
        // TC02:  Verifies that an exception is thrown when two points are identical.
        assertThrows(IllegalArgumentException.class, () ->
                new Plane(new Point(1,1,1), new Point(1,1,1), new Point(2,2,2)),
                "ERROR: Plane constructor did not throw exception for identical points 1 and 2"
        );

        //TC03: Verifies that an exception is thrown when the first and third points are identical.
        assertThrows(IllegalArgumentException.class, () ->
                        new Plane(new Point(1,1,1), new Point(2,2,2), new Point(1,1,1)),
                "ERROR: Plane constructor did not throw exception for identical points 1 and 3");

        //TC04: Verifies that an exception is thrown when the second and third points are identical.
        assertThrows(IllegalArgumentException.class, () ->
                        new Plane(new Point(1,1,1), new Point(2,2,2), new Point(2,2,2)),
                "ERROR: Plane constructor did not throw exception for identical points 2 and 3");

        //TC05: Verifies that an exception is thrown when all three points are identical.
        assertThrows(IllegalArgumentException.class, () ->
                        new Plane(new Point(1,1,1), new Point(1,1,1), new Point(1,1,1)),
                "ERROR: Plane constructor did not throw exception for all identical points");

        //TC06: Verifies that an exception is thrown when the three points are collinear.
        assertThrows(IllegalArgumentException.class, () ->
                        new Plane(new Point(0,0,0), new Point(1,1,1), new Point(2,2,2)),
                "ERROR: Plane constructor did not throw exception for collinear points");

    }
}