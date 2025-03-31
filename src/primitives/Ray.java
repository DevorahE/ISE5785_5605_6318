package primitives;

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
     * @param head - Starting point - head of the ray
     * @param direction - Direction vector of the ray
     */
    public Ray(Point head, Vector direction)
    {
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

    public Point getHead() {
        return head;
    }

    public Vector getDirection() {
        return direction;
    }
}
