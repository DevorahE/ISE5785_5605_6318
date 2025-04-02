package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;


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
        double projection = axis.getDirection().dotProduct(point.subtract(axis.getHead())); //Calculate the projection of the point on tube's ray

        // If projection == 0, the point is perpendicular to the tube's axis at its origin,
        // so the normal is simply the vector from the axis head to the point.
        if (projection == 0)
             return point.subtract(axis.getHead()).normalize();


        //The point is on the body of the tube
        Point newCenter = axis.getHead().add(axis.getDirection().scale(projection));//Calculate the "new" center of the tube that is in front of the given point

        return point.subtract(newCenter).normalize();
    }

    @Override
    public List<Point> findIntsersections(Ray ray) {
        return null;
    }
}
