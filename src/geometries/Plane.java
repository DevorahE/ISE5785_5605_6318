package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;


/**
 * class Plane is a class representing a plane
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Plane extends Geometry{

    /**
     * point in plane
     */
    private final Point q;

    /**
     * normal vector to the plane
     */
    private final Vector normal;


    /**
     * Constructor to initialize Plane based on three points in plane
     *
     * @param p1 first point in plane
     * @param p2 second point in plane
     * @param p3 third point in plane
     */
    public Plane(Point p1, Point p2, Point p3 )
    {
        //Calculating normal by 3 points
        Vector v0 = p1.subtract(p2);
        Vector v1 = p3.subtract(p2);
        Vector v2 = v0.crossProduct(v1);

        this.normal = v2.normalize();;
        this.q= p1;
    }


    /**
     * Constructor to initialize Plane based on a normal vector and point in plane
     *
     * @param q point in plane
     * @param normal vector to plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }


    /**
     * getter to normal vector to plane normal
     *
     * @return
     */
    public Vector getNormal() {
        return normal;
    }


    @Override
    public List<Point> findIntsersections(Ray ray) {
        return null;
    }
}
