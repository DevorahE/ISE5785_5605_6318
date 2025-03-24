package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;


/**
 * class Tube is a class representing a tube
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Tube extends RadialGeometry{


    /**
     * axis ray of the tube
     */
    protected final Ray axis;

    /**
     * Constructor to initialize Tube based on an axis ray and the radius of the tube
     * *
     * @param radius radius of the tube
     * @param axis   axis ray of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
