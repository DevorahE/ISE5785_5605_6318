package geometries;

/**
 * Unit tests for primitives.TubeTest class
 * @author Devorah Wajs and Guila Czerniewicz
 */

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {


    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Point on side of tube at (1, 0, 5), expect normal (1,0,0)
        Tube tube = new Tube(1.0, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        Point surfacePoint = new Point(1, 0, 5);
        Vector expectedNormal = new Vector(1, 0, 0);
        assertEquals(expectedNormal, tube.getNormal(surfacePoint), "getNormal() returned incorrect normal vector");

        // =============== Boundary Values Tests ==================
        // TC02: Point on axis → should throw exception
        Point axisPoint = new Point(0, 0, 5);
        assertThrows(IllegalArgumentException.class,
                () -> tube.getNormal(axisPoint),
                "getNormal() did not throw on axis point");
    }
}