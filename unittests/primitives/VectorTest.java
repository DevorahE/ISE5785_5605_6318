package primitives;

/**
 * Unit tests for primitives.Vector class
 * @author Devorah Wajs and Guila Czerniewicz
 */


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Method for testing operations on single vector
     */


    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        Vector v1 = new Vector(1, 2, 3);
        assertDoesNotThrow(() -> v1, "Failed constructing a vector with 3 coordinates");

        // TC02: current vector with the other ctor
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "Failed constructing a vector with Double3 param");

        // =============== Boundary Values Tests ==================
        // TC03: Zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Constructed a zero vector");

        // TC04: Zero vector with the other ctor
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        Vector v1 = new Vector(1, 2, 2);
        assertEquals(9, v1.lengthSquared(), DELTA, "ERROR: lengthSquared() wrong value");

        // =============== Boundary Values Tests ==================
        // TC02: positive and negative vector
        Vector v2 = new Vector(-1, 2, -2);
        assertEquals(9, v2.lengthSquared(), DELTA, "ERROR: lengthSquared() wrong value with positive and negative vector");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        Vector v1= new Vector(-1, -2, -2);
        assertEquals(3, v1.length(), DELTA, "ERROR: length() wrong value");

        // =============== Boundary Values Tests ==================
        // TC02: positive and negative vector
        Vector v2= new Vector(-1, 2, -2);
        assertEquals(3, v2.length(), DELTA, "ERROR: length() wrong value");
    }


    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        Vector v1= new Vector(2, 3, 4);
        assertEquals(1, v1.normalize().length(), "ERROR: the normalized vector is not a unit vector");

        // =============== Boundary Values Tests ==================
        // TC02: zero vector
        Vector v2 = new Vector(1, 4, 7);
        assertThrows(IllegalArgumentException.class, () -> v2.normalize().crossProduct(v2), "ERROR: the normalized vector is not parallel to the original one");

        //TC03: v.normalized*v bigger than 0
        Vector v3 = new Vector(1, 2, 3);
        assertTrue(v3.normalize().dotProduct(v1) > 0, "ERROR: the normalized vector is opposite to the original one");
    }


    /**
     * Method for testing operations on two vectors
     */


    /**
     * Test method for {@link primitives.Vector#add(Vector)}
     */
    @Test
    void testAdd() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 2, -3);
        Vector v3 = new Vector(5, 4, 0);
        assertEquals(v3, v1.add(v2), "ERROR: Vector + Vector does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC02: result is zero vector
       Vector v1_OPPOSITE = new Vector(-1, -2, -3);
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1_OPPOSITE), "ERROR: Vector + -itself does not throw an exception");
    }


    /**
     * Test method for {@link primitives.Vector#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtract two points
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(2, 4, 6);
        Point p1 = new Point(1, 2, 3);

        assertEquals(v1, v2.subtract(p1), "Subtract two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Subtract equal points
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "Subtract equal points does not work correctly");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}
     */
    @Test
    void testScale() {
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}
     */
    @Test
    void testDotProduct() {
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}
     */
    @Test
    void testCrossProduct() {
    }


}