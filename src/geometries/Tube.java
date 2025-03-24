package geometries;

import primitives.Point;
import primitives.Vector;

public class Tube extends RadialGeometry{

    public Tube(double radius) {
        super(radius);
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
