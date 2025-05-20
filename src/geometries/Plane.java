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
    public List<Intersection> calculateIntersectionsHelper(Ray ray){
        // Check if the ray is parallel to the plane (dot product equals zero)
        // or if the ray's starting point is the same as the plane's reference point
        if(ray.getDirection().dotProduct(normal) == 0 || q.equals(ray.getPoint(0))) {
            return null;
        }

        // Calculate parameter t using the plane-ray intersection formula
        double t = normal.dotProduct(q.subtract(ray.getPoint(0))) / normal.dotProduct(ray.getDirection());

        // If t is less than or equal to zero, the intersection point is behind the ray origin
        if (t <= 0) {
            return null;
        }

        // Calculate and return the intersection point using the ray equation: p = ray.head + t * ray.direction
        return List.of(new Intersection(this, ray.getPoint(t)));

    }
}
