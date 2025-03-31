package geometries;
/**
 * Unit tests for primitives.SphereTest class
 * @author Devorah Wajs and Guila Czerniewicz
 */
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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


}