package geometries;

import primitives.*;

/**
 * class Geometries is a class representing a set of geometric shapes
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */

public abstract class Geometry implements Intersectable {

    /**
     * abstract function to get the normal of the vector
     */
    public abstract Vector getNormal(Point point);

}
