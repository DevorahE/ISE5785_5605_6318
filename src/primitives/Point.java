package primitives;

import java.util.Objects;
import java.util.Vector;

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

    public Vector subtract(Point point)
    {
        //TODO
        return  new Vector<>();
    }

    public Point add(Vector vector)
    {
        //TODO
        return  new Point(0,0,0);
    }

    public double distanceSquared(Point point)
    {
        //TODO
        return 0;
    }

    public double distance(Point point)
    {
        //TODO
        return 0;
    }

}
