package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;


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
        return point.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {

        // Case when the ray starts at the center of the sphere
        if (center.equals(ray.getHead()))
            return List.of(ray.getHead().add(ray.getDirection().scale(radius)));

        // Vector from the ray's head to the center of the sphere
        Vector u = (center.subtract(ray.getHead()));
        // Projection of u onto the ray direction
        double tm = ray.getDirection().dotProduct(u);
        // Distance from the sphere center to the closest point on the ray
        double d = Math.sqrt(u.lengthSquared() - tm * tm);

        // If the distance is greater than the radius or the sphere is behind the ray
        if (d >= radius || tm < 0)
            return null; //There aren't intersections

        // Compute th, which is the distance from the closest point to the intersection points
        double th = Math.sqrt(radius * radius - d * d);
        double t1 = tm - th;
        double t2 = tm + th;

        // If both intersection points are in front of the ray
        if (t1 > 0 && t2 > 0)
            return List.of(ray.getHead().add(ray.getDirection().scale(t1)), ray.getHead().add(ray.getDirection().scale(t2)));
        if (t1 > 0)
            return List.of(ray.getHead().add(ray.getDirection().scale(t1)));

        // We know that t2 > 0, so we return the second intersection point
        return List.of(ray.getHead().add(ray.getDirection().scale(t2)));
    }
}
