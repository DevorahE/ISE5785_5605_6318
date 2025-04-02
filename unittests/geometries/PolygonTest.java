package geometries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import geometries.Plane;
import geometries.Polygon;
import primitives.*;
/**
 * Testing Polygons
 * @author Dan
 */

class PolygonTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "ERROR: Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "ERROR: Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "ERROR: Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "ERROR: Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "ERROR: Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "ERROR: Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "ERROR: Constructed a polygon with vertice on a side");

    }

    /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "ERROR: Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "ERROR: Polygon's normal is not orthogonal to one of the edges");
    }

    /** Test method for {@link geometries.Polygon#findIntersections(Ray)} */
    @Test
    void testFindIntersections() {
        Polygon polygon = new Polygon(new Point(1, 1, 0), new Point(1, 0, 0), new Point(-1, -1, 0), new Point(0, 1, 0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: the intersection point is inside the Polygon
        assertEquals(1, polygon.findIntersections(
                        new Ray(new Point(-0.5, -0.5, 1), new Vector(0, 0, -1))).size(), DELTA,
                "ERROR: Failed to find the intersection point when the intersection point is inside the Polygon");

        // TC02: the intersection point is outside the Polygon and against an edge
        assertNull(polygon.findIntersections(
                        new Ray(new Point(0.5, 2, 1), new Vector(0, 0, -1))),
                "ERROR: Failed to find the intersection point when the intersection point is outside the Polygon and against an edge");

        // TC03: the intersection point is outside the Polygon and against a vertex
        assertNull(polygon.findIntersections(
                        new Ray(new Point(2, 2, 1), new Vector(0, 0, -1))),
                "ERROR: Failed to find the intersection point when the intersection point is outside the Polygon and against an edge");

        // ================= Boundary Values Tests =================
        // TC04: the intersection point is on the edge of the Polygon
        assertNull(polygon.findIntersections(
                        new Ray(new Point(0.5, 1, -1), new Vector(0, 0, 1))),
                "ERROR: Failed to find the intersection point when the intersection point is on the edge of the Polygon");

        // TC05: the intersection point is on the vertex of the Polygon
        assertNull(polygon.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "ERROR: Failed to find the intersection point when the intersection point is on the vertex of the Polygon");

        // TC06: the intersection point is outside the triangle and on edge's continuation
        assertNull(polygon.findIntersections(
                        new Ray(new Point(2, 1, -1), new Vector(0, 0, 1))),
                "ERROR:  Failed to find the intersection point when the intersection point is outside the triangle and on edge's continuation");

    }
}