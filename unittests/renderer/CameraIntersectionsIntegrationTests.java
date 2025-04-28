package renderer;
import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for ray intersections with Sphere, Plane and Triangle.
 */
public class CameraIntersectionsIntegrationTests {

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(1).setVpSize(3, 3);

    /**
     * Helper method to build camera, shoot rays and count intersections.
     */
    private void assertCountIntersections(Geometry geometry, Point cameraLocation, int expectedCount) throws CloneNotSupportedException {
        cameraBuilder.setLocation(cameraLocation);
        cameraBuilder.build();
        assertEquals(expectedCount, getIntersections(geometry).size(), "Wrong number of intersections");
    }

    /**
     * Test ray construction and intersection count with spheres.
     */
    @Test
    void testConstructRayWithSphere() throws CloneNotSupportedException {
        assertCountIntersections(new Sphere(new Point(0, 0, -3), 1), new Point(0, 0, 0), 2);
        assertCountIntersections(new Sphere(new Point(0, 0, -2.5), 2.5), new Point(0, 0, 0.5), 18);
        assertCountIntersections(new Sphere(new Point(0, 0, -2), 2), new Point(0, 0, 0.5), 10);
        assertCountIntersections(new Sphere(new Point(0, 0, 0), 4), new Point(0, 0, 0.5), 9);
        assertCountIntersections(new Sphere(new Point(0, 0, 1), 0.5), new Point(0, 0, 0), 0);
    }

    /**
     * Test ray construction and intersection count with planes.
     */
    @Test
    void testConstructRayWithPlane() throws CloneNotSupportedException {
        assertCountIntersections(new Plane(new Point(0, 0, -1), new Point(1, 0, -1), new Point(0, 1, -1)), new Point(0, 0, 1), 9);
        assertCountIntersections(new Plane(new Point(0, 0, -2), new Point(-3, 0, 0), new Point(-3, 2, 0)), new Point(0, 0, 1), 9);
        assertCountIntersections(new Plane(new Point(0, 0, -4), new Point(-3, 0, 0), new Point(-3, 2, 0)), new Point(0, 0, 1), 6);
    }

    /**
     * Test ray construction and intersection count with triangles.
     */
    @Test
    void testConstructRayWithTriangle() throws CloneNotSupportedException {
        assertCountIntersections(new Triangle(new Point(0, 1, -2), new Point(-1, -1, -2), new Point(1, -1, -2)), new Point(0, 0, 0.5), 1);
        assertCountIntersections(new Triangle(new Point(0, 20, -2), new Point(-1, -1, -2), new Point(1, -1, -2)), new Point(0, 0, 1), 2);
    }
}

