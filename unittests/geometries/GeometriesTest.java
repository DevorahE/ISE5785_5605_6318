package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for primitives.GeometriesTest class
 * @author Devorah Wajs and Guila Czerniewicz
 */
class GeometriesTest {


    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {

       Geometries geometries = new Geometries(new Sphere(1, new Point(0, 0, 1)),
                new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0)),
                new Plane(new Point(0, 0, 3), new Vector(0, 0, 1)));

        // ============ Equivalence Partitions Tests ==============

        // TC01: some geometries are intersected
        assertEquals(3, geometries.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 1, 1))).size(), "some geometries are intersected");

        // ================= Boundary Values Tests =================

        // TC02: empty geometries list
        assertNull(new Geometries().findIntersections(new Ray(new Point(1,1,1), new Vector(1,1,1))), "empty geometries list");

        // TC03: no geometry is intersected
        assertNull(geometries.findIntersections(new Ray(new Point(1,1,2.5), new Vector(1,0,0))), "no geometry is intersected");

        // TC04: one geometry is intersected
        assertEquals(2, geometries.findIntersections(new Ray(new Point(0, -2, 1), new Vector(0, 1, 0))).size(), "one geometry is intersected");

        // TC05: all geometries are intersected
        assertEquals(4, geometries.findIntersections(new Ray(new Point(0.6, 0.6, -2), new Vector(0, 0, 1))).size(), "all geometries are intersected");

    }
}