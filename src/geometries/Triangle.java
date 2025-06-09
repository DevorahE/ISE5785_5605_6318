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

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        var intersections = plane.calculateIntersections(ray, maxDistance);
        if (intersections == null || intersections.getFirst().point.equals(vertices.get(0)))
            return null;

        Point intersect = intersections.getFirst().point;

        Vector v1 = vertices.get(2).subtract(vertices.get(0));
        Vector v2 = vertices.get(1).subtract(vertices.get(0));
        Vector v3 = intersect.subtract(vertices.get(0));

        double dot00 = v1.dotProduct(v1);
        double dot01 = v1.dotProduct(v2);
        double dot02 = v1.dotProduct(v3);
        double dot11 = v2.dotProduct(v2);
        double dot12 = v2.dotProduct(v3);

        double denominator = dot00 * dot11 - dot01 * dot01;

        double u = alignZero((dot11 * dot02 - dot01 * dot12) / denominator);
        double v = alignZero((dot00 * dot12 - dot01 * dot02) / denominator);

        if ((u > 0) && (v > 0) && (u + v < 1))
            return List.of(new Intersection(this, intersect));

        return null;
    }
}
