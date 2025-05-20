package geometries;

import primitives.*;

/**
 * class Geometries is a class representing a set of geometric shapes
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */

public abstract class Geometry extends Intersectable {




    protected Color emission = Color.BLACK;

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * abstract function to get the normal of the vector
     */
    public abstract Vector getNormal(Point point);

}
