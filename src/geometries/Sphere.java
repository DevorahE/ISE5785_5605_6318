package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere extends RadialGeometry{
    public Sphere(double radius) {
        super(radius);
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
