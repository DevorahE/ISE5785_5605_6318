package geometries;

import primitives.*;

import java.util.List;
import static primitives.Util.*;

/**
 * class Triangle is a basic class representing a triangle
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Triangle extends Polygon{
    /**
     * Constructs a Triangle object with three given points.
     *
     * @param p1 - the first point of the triangle
     * @param p2 - the second point of the triangle
     * @param p3 - the third point of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    public List<Point> findIntersections(Ray ray) {

        //  Calculate vectors for the triangle edges
        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
        Vector edge2 = vertices.get(2).subtract(vertices.get(0));

        Vector h = ray.getDirection().crossProduct(edge2);
        double a = alignZero(edge1.dotProduct(h));

        // Ray is parallel to the triangle
        if (isZero(a)) {
            return null;
        }

        double f = 1 / a;
        Vector s = ray.getPoint(0).subtract(vertices.get(0));
        double u = f * alignZero(s.dotProduct(h));

        // Intersection point is outside the triangle
        if (u <= 0 || u >= 1) {
            return null;
        }

        Vector q = s.crossProduct(edge1);
        double v = f * ray.getDirection().dotProduct(q);

        // Intersection point is outside the triangle
        if (v <= 0 || u + v >= 1) {
            return null;
        }

        // Compute intersection distance along the ray
        double t = alignZero(f * edge2.dotProduct(q));

        // Intersection is behind the ray's origin
        if (t <= 0) {
            return null;
        }

        // Return the intersection point
        return List.of(ray.getPoint(t));
    }
}
