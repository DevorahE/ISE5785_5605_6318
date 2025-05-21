package primitives;

/**
 * Class Point is the basic class representing a point of geometry
 * 3-Dimensional coordinate system.
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Point {

    /**
     * Represents a constant representing the zero point.
     */
    protected final Double3 xyz ;

    /**
     * 3-dimensional coordinates
     */
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * Constructor to initialize Point based object with 3 number values
     *
     * @param x first number value
     * @param y second number value
     * @param z third number value
     */
    public Point(double x, double y, double z)
    {
        xyz = new Double3(x,y,z);
    }

    /**
     * Constructor to initialize Point based object with one 3 double numbers (Double3) value
     *
     * @param xyz 3 double numbers (Double3) value
     */
    public Point(Double3 xyz)
    {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "" + xyz;
    }

    /**
     * subtracts two points into a new vector from the second
     * point (right handle side) to the first one
     *
     * @param point
     * @return vector
     */
    public primitives.Vector subtract(Point point)
    {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    /**
     * subtracts two points into a new vector from the second
     * point (right handle side) to the first one
     *
     * @param vector
     * @return point
     */
    public Point add(primitives.Vector vector)
    {
        return  new Point(this.xyz.add(vector.xyz));
    }

    /**
     * calculates the distance between two points - squared
     *
     * @param point right handle side operand for distance squared calculation
     * @return distance between points - squared
     */
    public double distanceSquared(Point point)
    {
        double dx = this.xyz.d1() - point.xyz.d1();
        double dy = this.xyz.d2() - point.xyz.d2();
        double dz = this.xyz.d3() - point.xyz.d3();
        return dx * dx + dy * dy + dz * dz;

    }

    /**
     * calculates the distance between two points
     *
     * @param point right handle side operand for distance calculation
     * @return distance between points
     */
    public double distance(Point point)
    {
        return Math.sqrt(distanceSquared(point));
    }

}
