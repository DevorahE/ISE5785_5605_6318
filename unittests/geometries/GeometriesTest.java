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
    /**
     * Test method for {@link Geometries#calculateIntersections(Ray, double)}.
     */
    @Test
    void testCalculateIntersections() {
        // The axis vector of ray to (0,0,1)
        final Vector v001 = new Vector(0, 0, 1);
        // A ray for test
        final Ray ray = new Ray(new Point(1, 1, 1), v001);

        // A polygon used in some test cases - 1 intersection with ray
        final Polygon polygon = new Polygon(
                new Point(0, 2, 1),
                new Point(2, 2, 1),
                new Point(2, -1, 2),
                new Point(0, -1, 2)
        );
        // A triangle used in some test cases - 1 intersection with ray
        final Triangle triangle = new Triangle(
                new Point(0, 2, 2),
                new Point(2, 2, 2),
                new Point(0, -1, 4)
        );

        // A sphere used in some test cases - 1 intersection1 with ray
        final Plane plane = new Plane(new Point(-1, 3, 3), v001);
        // A sphere used in some test cases - 2 intersections with ray
        final Sphere sphere = new Sphere(1, new Point(1, 1, 4));
        // A tube used in some test cases - 1 intersection with ray
        final Tube tube = new Tube(2, new Ray(new Point(1,1,0),new Vector(0,1,0)));
        // A cylinder used in some test cases - 2 intersections with ray
        final Cylinder cylinder = new Cylinder(2, new Ray(new Point(1,1,6),v001), 6);

        Geometries geometries = new Geometries(plane, polygon, triangle, sphere, tube, cylinder);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some intersections within range and some not
        var result = geometries.calculateIntersections(ray, 1.8);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // TC01: No intersections within range at all
        assertNull(geometries.calculateIntersections(ray, 0.3),
                "ERROR: the intersections' array should be null");

        // TC02: Only one intersection within range
        result = geometries.calculateIntersections(ray, 0.7);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // TC03: Ray ends at some intersections
        result = geometries.calculateIntersections(ray, 2);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: the intersections' array should not be null");
    }

}