package geometries;

import primitives.Point;
import primitives.Vector;

public class Cylinder extends Tube{
    public Cylinder(double radius) {
        super(radius);
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
