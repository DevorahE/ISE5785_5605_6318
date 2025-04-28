package renderer;
import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for intersections between Camera rays and Geometries.
 * Testing the integration of Camera's ray generation and Geometries' findIntersections method.
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * Integration tests for intersections between Camera rays and Geometries.
     * Testing the integration of Camera's ray generation and Geometries' findIntersections method.
     */
    private void assertCountIntersections(Camera camera, Intersectable geometry, int expectedCount) throws CloneNotSupportedException {
        int count = 0;
        int nX = 3; // View plane width in pixels
        int nY = 3; // View plane height in pixels

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }

        assertEquals(expectedCount, count, "ERROR: Wrong number of intersections");
    }

    /**
     * Test ray construction and intersection count with spheres.
     */
    @Test
    void testSphereIntersections() throws CloneNotSupportedException {
        Camera camera = new Camera(new Point(0, 0, 0), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPSize(3, 3)
                .setVPDistance(1);

        // Sphere centered at (0, 0, -3) with radius 1
        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);

        assertCountIntersections(camera, sphere, 2); // Expected 2 intersections
    }
            }

    /**
     * Test ray construction and intersection count with planes.
     */
    @Test
    void testPlaneIntersections() throws CloneNotSupportedException {
        assertCountIntersections(new Plane(new Point(0, 0, -1), new Point(1, 0, -1), new Point(0, 1, -1)), new Point(0, 0, 1), 9);
        assertCountIntersections(new Plane(new Point(0, 0, -2), new Point(-3, 0, 0), new Point(-3, 2, 0)), new Point(0, 0, 1), 9);
        assertCountIntersections(new Plane(new Point(0, 0, -4), new Point(-3, 0, 0), new Point(-3, 2, 0)), new Point(0, 0, 1), 6);
    }

    /**
     * Test ray construction and intersection count with triangles.
     */
    @Test
    void testTriangleIntersections() throws CloneNotSupportedException {
        assertCountIntersections(new Triangle(new Point(0, 1, -2), new Point(-1, -1, -2), new Point(1, -1, -2)), new Point(0, 0, 0.5), 1);
        assertCountIntersections(new Triangle(new Point(0, 20, -2), new Point(-1, -1, -2), new Point(1, -1, -2)), new Point(0, 0, 1), 2);
    }
}

