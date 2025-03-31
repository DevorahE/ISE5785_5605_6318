package geometries;
/**
 * Unit tests for primitives.CylinderTest class
 * @author Devorah Wajs and Guila Czerniewicz
 */
import org.junit.jupiter.api.Test;
import primitives.*;
import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private final double DELTA = 0.000001;


    /**
     /** Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}. */
    @Test
    void testGetNormal() {

        // Create a cylinder with a radius of 2, axis passing through the origin (0, 0, 0) in the Z direction, and a height of 4
        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));  // Axis along the Z-axis
        Cylinder cylinder = new Cylinder(2, axis, 4);

        // Test with a point on the cylinder surface, e.g., (2, 0, 2)
        Point pointOnCylinder = new Point(2, 0, 2);

        // The expected normal vector at this point: (2, 0, 2) - (0, 0, 0) = (2, 0, 2)
        // Project this onto the plane perpendicular to the axis (Z-axis in this case), resulting in the vector (2, 0, 0)
        // After normalization: (1, 0, 0)
        Vector expectedNormal = new Vector(1, 0, 0);

        // Call the getNormal method
        Vector normal = cylinder.getNormal(pointOnCylinder);

        // Check if the normal vector is a unit vector (length = 1)
        assertEquals(1, normal.length(), DELTA, "The normal vector is not a unit vector");

        // Check if the normal vector is in the correct direction (it should be perpendicular to the axis and in the plane of the cylinder)
        assertEquals(expectedNormal, normal, "The normal vector is incorrect");

        // Test with another point on the cylinder, e.g., (0, 2, 1)
        pointOnCylinder = new Point(0, 2, 1);

        // Expected normal vector: (0, 2, 1) - (0, 0, 0) = (0, 2, 1)
        // Project this onto the plane perpendicular to the axis (Z-axis), resulting in the vector (0, 2, 0)
        // After normalization: (0, 1, 0)
        expectedNormal = new Vector(0, 1, 0);

        // Call the getNormal method again
        normal = cylinder.getNormal(pointOnCylinder);

        // Check if the normal vector is a unit vector (length = 1)
        assertEquals(1, normal.length(), DELTA, "The normal vector is not a unit vector");

        // Check if the normal vector is in the correct direction
        assertEquals(expectedNormal, normal, "The normal vector is incorrect");
    }
}