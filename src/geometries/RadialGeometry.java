package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * class radialGeometry is a class representing a geometry
 * with a radius
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public abstract class RadialGeometry extends Geometry{

    /**
     * the geometry radius
     */
   protected final double radius;


    /**
     * Constructor to initialize radialGeometry based on a radius
     *
     * @param radius
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
