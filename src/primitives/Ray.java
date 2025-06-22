package primitives;
import geometries.Intersectable.Intersection;

import targetAreas.TargetArea;

import java.util.LinkedList;
import java.util.List;

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
     * A small delta value used to move beginning of the rays
     */
    private static final double DELTA = 0.1;


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

    /**
     * Constructor to initialize Ray based on point, direction vector, and a normal vector.
     *
     * @param head    - Starting point - head of the ray
     * @param direction - Direction vector of the ray
     * @param normal  - Normal vector to adjust the head position
     */
    public Ray(Point head, Vector direction, Vector normal) {
        this.direction = direction.normalize();
        double nv = normal.dotProduct(this.direction);

        // Add a small delta to the ray's origin to avoid floating-point precision issues
        if (!Util.isZero(nv)) {
            this.head = head.add(normal.scale(nv > 0 ? DELTA : -DELTA));
        } else {
            this.head = head;
        }
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
        try {
            return head.add(direction.scale(t));
        } catch (IllegalArgumentException e) {
            return head;
        }
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
    public Point findClosestPoint(List<Point> listPoints) {
        return listPoints == null ? null
                : findClosestIntersection(listPoints.stream().map(p -> new Intersection(null, p)).toList()).point;
    }
    /**
     * Finds the closest intersection point from a list of intersections.
     *
     * @param intersections the list of intersections to search for the closest one
     * @return the closest intersection point, or null if the list is empty or null
     */
    public Intersection findClosestIntersection(List<Intersection> intersections)
    {
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        // Initialize the closest point and minimum distance
        Intersection closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Intersection intersection : intersections) {
            double distanceSquared = head.distanceSquared(intersection.point);
            if (distanceSquared < minDistance) {
                minDistance = distanceSquared;
                closest = intersection;
            }
        }
        return closest;
    }
    /**
     * Generates a list of rays ("beam") according to a given target area
     *
     * @param targetArea the area the beam is directed through
     * @return a list of rays representing the beam
     */
    public List<Ray> createBeam(TargetArea targetArea) {
        List<Ray> beamRays = new LinkedList<>();
        List<Point> beamPoints = targetArea.generatePoints();
        for (Point point : beamPoints)
            beamRays.add(new Ray(head, point.subtract(head)));
        return beamRays;
    }

    /**
     * Generates a list of rays ("beam") according to a given target area
     *
     * @param targetArea the area the beam is directed from
     * @param distance the distance of the point the beam is directed to
     * @return a list of rays representing the beam
     */
    public List<Ray> createBeamReverse(TargetArea targetArea, double distance) {
        List<Ray> beamRays = new LinkedList<>();
        List<Point> beamPoints = targetArea.generatePoints();
        Point focalPoint = this.getPoint(distance);
        for (Point point : beamPoints)
            beamRays.add(new Ray(point, focalPoint.subtract(point)));
        return beamRays;
    }
}
