package primitives;

import java.util.List;
import java.util.Objects;

/**
 * Class Ray is the basic class representing a ray of geometry
 * 3-Dimensional coordinate system.
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */

public class Ray {
    /**
     * Starting point - head of the ray
     */
    private final Point head;

    /**
     * Direction vector of the ray
     */
    private final Vector direction;

    /**
     * Constructor to initialize Ray based on point and a vector
     *
     * @param head      - Starting point - head of the ray
     * @param direction - Direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    /**
     * Calculates a point on the ray at a given distance from the head.
     *
     * @param t the scalar distance from the head of the ray. If t is zero, the head point is returned
     * @return the point on the ray at the specified distance t from the head.
     */
    public Point getPoint(double t) {
        // if t is zero, return the head point
        if (Util.isZero(t))
            return head;
        return head.add(direction.scale(t));
    }


    public Vector getDirection() {
        return direction;
    }

    /**
     * Finds the closest point to the ray's head from a list of points.
     * Currently, this method is not implemented and always returns null.
     *
     * @param listPoints the list of points to search for the closest point
     * @return the closest point to the ray's head, or null if not implemented
     */
    public Point findClosestPoint(List<Point> listPoints)
    {
        if (listPoints == null || listPoints.isEmpty()) {
            return null;
        }
        // Initialize the closest point and minimum distance
        Point closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point point : listPoints) {
            double distance = head.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = point;
            }
        }
        return closest;
    }
}
