package geometries;

import primitives.*;

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
        // Compute vectors from the ray's head to the triangle's vertices
        Vector v1 = vertices.get(0).subtract(ray.getPoint(0));
        Vector v2 = vertices.get(1).subtract(ray.getPoint(0));
        Vector v3 = vertices.get(2).subtract(ray.getPoint(0));

        // Compute normal vectors for the triangle's edges
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Compute dot products of the ray direction with the normal vectors
        double s1 = ray.getDirection().dotProduct(n1);
        double s2 = ray.getDirection().dotProduct(n2);
        double s3 = ray.getDirection().dotProduct(n3);

        // If any dot product is zero, the ray is parallel to the corresponding edge
        if (Util.isZero(s1) || Util.isZero(s2) || Util.isZero(s3)) {
            return null; // The ray is parallel to one of the edges
        }

        // Check if the signs of the dot products are consistent
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            // Create a plane from the triangle's vertices
            Plane plane = new Plane(vertices.get(0), vertices.get(1), vertices.get(2));
            // Find intersections with the plane
            return plane.findIntersections(ray);
        }

        return null; // No intersection with the triangle
    }
}
