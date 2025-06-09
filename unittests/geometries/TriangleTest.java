package geometries;


import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Triangle class
 * @author Devorah Wajs and Guila Czerniewicz
 */
class TriangleTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /** Test method for {@link geometries.Triangle#getNormal(primitives.Point)}. */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] points =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
        Triangle triangle = new Triangle(points[0], points[1], points[2]);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> triangle.getNormal(new Point(0, 0, 1)), "");

        // generate the test result
        Vector result = triangle.getNormal(new Point(0, 0, 1));

        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "ERROR: Triangle's normal is not a unit vector");

        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(points[i].subtract(points[i == 0 ? 3 : i - 1])), DELTA,
                    "ERROR: Triangle's normal is not orthogonal to one of the edges");
    }

    /** Test method for {@link geometries.Triangle#findIntersections(Ray)} */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(0, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0));

        // ============ Equivalence Partitions Tests ==============

        // TC01: the intersection point is inside the triangle
        assertEquals(1, triangle.findIntersections(
                        new Ray(new Point(1.2, 1.2, 1), new Vector(-1, -1, -1))).size(),
                "ERROR: Failed to find the intersection point when the intersection point is inside the triangle");

        // TC02: the intersection point is outside the triangle and against an edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, -1, 0), new Vector(0, 0, 1))),
                "ERROR: Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // TC03: the intersection point is outside the triangle and against a vertex
        assertNull(triangle.findIntersections(
                        new Ray(new Point(-0.2, -0.2, 0), new Vector(1, 1, 1))),
                "ERROR: Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // =============== Boundary Values Tests ==================
        // TC04: the intersection point is on the edge of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 0, -1), new Vector(0, 0, 1))),
                "ERROR: Failed to find the intersection point when the intersection point is on the edge of the triangle");

        // TC05: the intersection point is in the vertex of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0, 0, -1), new Vector(0, 0, 1))),
                "ERROR: Failed to find the intersection point when the intersection point is in the vertex of the triangle");

        // TC06: the intersection point is outside the triangle and on edge's continuation
        assertNull(triangle.findIntersections(
                        new Ray(new Point(-1, 0, -1), new Vector(0, 0, 1))),
                "ERROR: Failed to find the intersection point when the intersection point is outside the triangle and on edge's continuation");
    }

    /**
     * Test method for {@link Triangle#calculateIntersections(Ray, double)}
     */
    @Test
    void calculateIntersections() {
        // ============ Equivalence Partitions Tests ==============
        final Triangle triangle = new Triangle(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 0));

        // Test Case 01 - Ray starts before triangle, and maxDistance is smaller than the distance between ray head and triangle
        Ray ray = new Ray(new Point(0.3, 0.3, 2), new Vector(0, 0, -1));
        assertNull(triangle.calculateIntersections(ray, 1),
                "ERROR: Ray's intersection point is greater than maxDistance");

        // Test Case 02 - Ray starts before triangle, and maxDistance is greater than the distance between ray head and triangle
        var result = triangle.calculateIntersections(ray, 3);
        assertNotNull(result, "ERROR: the intersections' array should not be null");
        assertEquals(1, result.size(), "ERROR: Wrong number of intersections");

        // Test Case 03 - Ray starts after triangle
        ray = new Ray(new Point(0.3, 0.3, -1), new Vector(0, 0, -1));
        assertNull(triangle.calculateIntersections(ray, 1),
                "ERROR: Wrong number of intersections");

        // =============== Boundary Values Tests ==================
        // Test Case 01 - Ray ends at triangle
        ray = new Ray(new Point(0.3, 0.3, 1), new Vector(0, 0, -1));
        assertNull(triangle.calculateIntersections(ray, 1),
                "ERROR: Wrong number of intersections");
    }
}