package geometries;

import primitives.Point;
import primitives.Vector;


/**
 * class Sphere is a class representing a sphere
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Sphere extends RadialGeometry{

    /**
     * center point of the sphere
     */
    private final Point center;


    /**
     * Constructor to initialize Sphere based on a center point and a radius of the sphere
     *
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }


    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
