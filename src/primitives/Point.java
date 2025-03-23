package primitives;

import java.util.Objects;

public class Point {

    protected final Double3 xyz ;
    public static Point ZERO = new Point(0,0,0);

    public Point(double x, double y, double z)
    {
        xyz = new Double3(x,y,z);
    }

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
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }

    public primitives.Vector subtract(Point point)
    {
        return new Vector(this.xyz.subtract(point.xyz));
    }

    public Point add(primitives.Vector vector)
    {
        return  new Point(this.xyz.add(vector.xyz));
    }

    public double distanceSquared(Point point)
    {
        double dx = this.xyz.d1() - point.xyz.d1();
        double dy = this.xyz.d2() - point.xyz.d2();
        double dz = this.xyz.d3() - point.xyz.d3();
        return dx * dx + dy * dy + dz * dz;

    }

    public double distance(Point point)
    {
        return Math.sqrt(distanceSquared(point));
    }

}
