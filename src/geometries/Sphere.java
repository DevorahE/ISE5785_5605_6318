package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;


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
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        Point p0 = ray.getPoint(0);
        Vector dir = ray.getDirection();

        // if the ray starts at the center of the sphere
        if (center.equals(p0))
            return List.of(new Intersection(this, ray.getPoint(radius)));

        Vector u = (center.subtract(p0));
        double tm = dir.dotProduct(u);
        double d = Util.alignZero(Math.sqrt(u.lengthSquared() - tm * tm));
        if (d >= radius)
            return null;

        double th = Math.sqrt(radius * radius - d * d);
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);

        // if the ray starts before the sphere
        if (t1 > 0 && t2 > 0 && alignZero(t1 - maxDistance) <= 0d && alignZero(t2 - maxDistance) <= 0d)
            return List.of(new Intersection(this,ray.getPoint(t1)),new Intersection(this, ray.getPoint(t2)));

        // if the ray starts inside the sphere
        if (t1 > 0 && alignZero(t1 - maxDistance) <= 0d)
            return List.of(new Intersection(this,ray.getPoint(t1)));
        if (t2 > 0 && alignZero(t2 - maxDistance) <= 0d)
            return List.of(new Intersection(this,ray.getPoint(t2)));

        return null;

    }
}
