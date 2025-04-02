package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

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
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
