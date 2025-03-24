package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * class Cylinder is a class representing a cylinder
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */

public class Cylinder extends Tube{

    /**
     * height of the tube
     */
    private final double height;


    /**
     * Constructor to initialize Cylinder based on given axis ray, radius, and height
     *
     * @param radius
     * @param axis
     * @param height
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
