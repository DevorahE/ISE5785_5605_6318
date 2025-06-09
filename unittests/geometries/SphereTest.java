package geometries;
/**
 * Unit tests for primitives.SphereTest class
 * @author Devorah Wajs and Guila Czerniewicz
 */
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private final double DELTA = 0.000001;


    /**
     *  Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        Point p1 = new Point(1, 0, 0);
        assertEquals(p1, new Sphere(1, p1).getNormal(new Point(2, 0, 0)), "Failed to get the normal vector of the sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {

        double sqrt075 = Math.sqrt(0.75);
        Vector v1 = new Vector(1, 0, 1);
        Vector v2 = new Vector(0, -1, 0);
        Vector v3 = new Vector(0, 1, 0);
        Vector v4 = new Vector(1, 1, 1);
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(0, 1, 1);
        Point p3 = new Point(0, 2, 1);
        Point p4 = new Point(0, -1, 1);
        Point p5 = new Point(0, 0.5, 1);
        Point p7 = new Point(0, sqrt075, 1.5);
        Sphere sphere = new Sphere(1, p1);

        // ============ Equivalence Partitions Tests ==============

        // TC01 The ray start inside the sphere
        assertEquals(List.of(p7), sphere.findIntersections(new Ray(new Point(0, 0, 1.5), v3)),
                "ERROR: Failed to find the intersection point when the ray start inside the sphere");

        // TC02 The ray never intersect the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 3), v4)),
                "ERROR: Failed to find the intersection point when the ray never intersect the sphere");

        // TC03 The ray start outside the sphere and intersect the sphere twice
        assertEquals(List.of(p7, new Point(0, -sqrt075, 1.5)), sphere.findIntersections(new Ray(new Point(0, 2, 1.5), v2)),
                "ERROR: Failed to find the intersection points when the ray start outside the sphere and intersect the sphere twice");

        // TC04 The ray start outside the sphere and the ray does not intersect the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0, -2, 1.5), v2)),
                "ERROR: Failed to find the intersection points when the ray start outside the sphere and not intersect the sphere");

        // =============== Boundary Values Tests =================

        // Test orthogonal rays:

        // TC05 The ray is orthogonal to the sphere and start before the sphere
        assertNull(sphere.findIntersections(new Ray(p3, new Vector(0, 0, 1))),
                "ERROR: Failed to find the intersection point when the ray never intersect the sphere");

        // TC06 The ray is orthogonal to the sphere and start in the sphere
        assertEquals(List.of(new Point(0, 0.5, 1 - sqrt075)), sphere.findIntersections(new Ray(p5, new Vector(0, 0, -1))),
                "ERROR: Failed to find the intersection point when the ray start inside the sphere");

        //tests for tangential ray:

        // TC07 The ray is tangential to the sphere and start before the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 1, 0), v1)),
                "ERROR: Failed to find the intersection point when the ray never intersect the sphere");

        // TC08 The ray is tangential to the sphere and start on the sphere
        assertNull(sphere.findIntersections(new Ray(p2, v1)),
                "ERROR: Failed to find the intersection point when the ray never intersect the sphere");

        // TC09 The ray is tangential to the sphere and start after the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(1, 1, 2), v1)),
                "ERROR: Failed to find the intersection point when the ray never intersect the sphere");


        //tests for rays that are not orthogonal nor tangential to the sphere(not reach middle of the sphere):

        // TC10 The ray start on the sphere and intersect the sphere
        assertEquals(List.of(new Point(-2.0 / 3, 1.0 / 3, 1.0 / 3)), sphere.findIntersections(new Ray(p2, new Vector(-1, -1, -1))),
                "ERROR: Failed to find the intersection point when the ray start on the sphere and intersect the sphere");

        // TC11 The ray start on the sphere and does not intersect the sphere
        assertNull(sphere.findIntersections(new Ray(p2, v4)),
                "ERROR: Failed to find the intersection point when the ray start on the sphere and doesn't intersect the sphere");

        //test that reach the middle of the sphere:

        // TC12 The ray start on the sphere and reach the middle of the sphere
        assertEquals(List.of(p4), sphere.findIntersections(new Ray(p2, v2)),
                "ERROR: Failed to find the intersection point when the ray start on the sphere and reach the middle of the sphere");

        // TC13 The ray start before the sphere and reach the middle of the sphere
        assertEquals(List.of(p2, p4), sphere.findIntersections(new Ray(p3, v2)).stream().sorted(Comparator.comparingDouble(p -> p.distance(new Point(-1, 0, 0)))).toList(),
                "ERROR: Failed to find the intersection point when the ray start before the sphere and reach the middle of the sphere");

        //TC14 The ray start in the middle of the sphere
        assertEquals(List.of(p2), sphere.findIntersections(new Ray(p1, v3)),
                "ERROR: Failed to find the intersection point when the ray start in the middle of the sphere");

        //TC15 the run on the sphere and does not reach the middle of the sphere because the direction is opposite
        assertNull(sphere.findIntersections(new Ray(p2, v3)),
                "ERROR: Failed to find the intersection point when the ray start on the sphere and doesn't reach the middle of the sphere");

        //TC16 the run after the sphere and does not reach the middle of the sphere because the direction is opposite
        assertNull(sphere.findIntersections(new Ray(p3, v3)),
                "ERROR: Failed to find the intersection point when the ray start after the sphere and doesn't reach the middle of the sphere");

        //TC17 the run in the sphere and does not reach the middle of the sphere because the direction is opposite
        assertEquals(List.of(p2), sphere.findIntersections(new Ray(p5, v3)),
                "ERROR: Failed to find the intersection point when the ray start in the sphere and doesn't reach the middle of the sphere");
    }
    /**
     * Test method for {@link Sphere#calculateIntersections(Ray, double)}
     */
    @Test
    void calculateIntersections() {
        final Sphere sphere = new Sphere(3, Point.ZERO);
        // A vector used in some test cases to (1,0,0)
        Vector v100 = new Vector(1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // Test Case 01 - Ray starts before sphere, and maxDistance is smaller than the distance between ray head and sphere
        Ray ray = new Ray(new Point(-6, 2.5, 0), v100);
        assertNull(sphere.calculateIntersections(ray, 3.5), "Ray's intersection point is greater than maxDistance");

        // TC02: Ray starts before the sphere and ends inside it
        ray = new Ray(new Point(-4, 1.5, 0), v100);
        var result = sphere.calculateIntersections(ray, 3.5);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        //TC03: Ray starts and ends inside the sphere
        ray = new Ray(new Point(-2, 0.5, 0), v100);
        assertNull(sphere.calculateIntersections(ray, 3.5), "ray starts and stops inside the sphere");

        // TC04: Ray starts inside the sphere and ends after it
        result = sphere.calculateIntersections(new Ray(new Point(2, -1.5, 0), v100), 3.5);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // TC05: Ray starts after sphere
        ray = new Ray(new Point(4, -2.5, 0), v100);
        assertNull(sphere.calculateIntersections(ray, 3.5), "ray starts after the sphere");

        // TC06: Ray crosses the sphere, starts before it and ends after it
        ray = new Ray(new Point(-4, 1.5, 0), v100);
        result = sphere.calculateIntersections(ray, 8);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(2, result.size(), "ERROR: Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // TC01: Ray starts before the sphere and ends at the first intersection point
        result = sphere.calculateIntersections(new Ray(new Point(-4, 0, 0), v100), 1);
        assertNull(result, "ERROR: the intersections' array should not be null");

        // TC02: Ray starts before the sphere and ends at the second intersection point
        result = sphere.calculateIntersections(new Ray(new Point(-4, 0, 0), v100), 7);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // TC03: Ray starts inside the sphere and ends at the intersection point
        result = sphere.calculateIntersections(new Ray(new Point(-2, 0, 0), v100), 5);
        assertNull(result, "ERROR: the intersections' array should not be null");
    }

}