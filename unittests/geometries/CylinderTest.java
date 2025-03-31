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
        Vector v1 = new Vector(0, 0, -1);
        Vector v2 = new Vector(0, 0, 1);
        Cylinder cylinder = new Cylinder(1, new Ray(Point.ZERO, v2), 1);
        // ============ Equivalence Partitions Tests ==============
        // TC01: the point is on the top outer surface of the cylinder
        assertEquals(new Vector(0, 1, 0),
                cylinder.getNormal(new Point(0, 1, 0.5)),
                "Bad normal to cylinder");

        // TC02: the point is on the bottom outer surface of the cylinder
        assertEquals(v1, cylinder.getNormal(new Point(0, 0.5, 0)),
                "Bad normal to cylinder");

        // TC03: the point is on the side outer surface of the cylinder
        assertEquals(v2, cylinder.getNormal(new Point(0, 0.5, 1)),
                "Bad normal to cylinder");

        // =============== Boundary Values Tests ==================
        // TC04: the point is on the top edge of the cylinder;
        assertEquals(v2, cylinder.getNormal(new Point(0, 1, 1)),
                "Bad normal to cylinder");

        // TC05: the point is on the bottom edge of the cylinder
        assertEquals(v1, cylinder.getNormal(new Point(0, 1, 0)),
                "Bad normal to cylinder");

        // TC06: the point is in the middle bottom outer surface of the cylinder
        assertEquals(v1, cylinder.getNormal(Point.ZERO),
                "Bad normal to cylinder");

        // TC07: the point is in the middle top edge of the cylinder
        assertEquals(v2, cylinder.getNormal(new Point(0, 0, 1)),
                "Bad normal to cylinder");


    } }