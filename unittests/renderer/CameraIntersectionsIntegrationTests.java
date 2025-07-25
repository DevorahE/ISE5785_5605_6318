package renderer;
import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for intersections between Camera rays and Geometries.
 * Testing the integration of Camera's ray generation and Geometries' findIntersections method.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * Integration tests for intersections between Camera rays and Geometries.
     * Testing the integration of Camera's ray generation and Geometries' findIntersections method.
     */
    private void assertCountIntersections(int expected, Intersectable geometry, Camera camera, int nX, int nY) throws CloneNotSupportedException {
        int count = 0;

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }

        assertEquals(expected, count, "ERROR: Wrong number of intersections");
    }

    /**
     * Test ray construction and intersection count with spheres.
     */
    @Test
    void testSphereIntersections() throws CloneNotSupportedException {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        // Small Sphere in front of the view plane
        Sphere sphere = new Sphere(1, new Point(0, 0, -3));
        assertCountIntersections(2, sphere, camera, 3, 3);

    }


    /**
     * Test ray construction and intersection count with planes.
     */
    @Test
    void testPlaneIntersections() throws CloneNotSupportedException {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        // Plane facing the camera
        Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        assertCountIntersections(9, plane, camera, 3, 3);
    }

    /**
     * Test ray construction and intersection count with triangles.
     */
    @Test
    void testTriangleIntersections() throws CloneNotSupportedException {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        // Small triangle in the center
        Triangle triangle = new Triangle(
                new Point(0, 1, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)
        );
    assertCountIntersections(1, triangle, camera, 3, 3);
    }
}

