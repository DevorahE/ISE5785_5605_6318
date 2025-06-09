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
        Point head = ray.getPoint(0);
        if (center.equals(head))
            return Util.alignZero(radius - maxDistance) < 0 ? List.of(new Intersection(this, ray.getPoint(radius))) : null;
        Vector u = center.subtract(head);
        double tm = u.dotProduct(ray.getDirection());
        double d = Math.sqrt(u.lengthSquared() - tm * tm);
        if (Util.alignZero(d - radius) >= 0)
            return null;
        double th = Math.sqrt(radius * radius - d * d);
        double t1 = Util.alignZero(tm - th);
        double t2 = Util.alignZero(tm + th);
        if (t1 > 0 && t2 > 0 && Util.alignZero(t1 - maxDistance) < 0 && Util.alignZero(t2 - maxDistance) < 0)
            return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
        else if (t1 > 0 && Util.alignZero(t1 - maxDistance) < 0)
            return List.of(new Intersection(this, ray.getPoint(t1)));
        else if (t2 > 0 && Util.alignZero(t2 - maxDistance) < 0)
            return List.of(new Intersection(this, ray.getPoint(t2)));

        return null;
    }
}
