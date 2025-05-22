package geometries;

import primitives.*;

/**
 * class Geometries is a class representing a set of geometric shapes
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */

public abstract class Geometry extends Intersectable {

    /**
     * The color of the geometry
     */
    protected Color emission = Color.BLACK;

    /**
     * A function that returns the color of the geometry
     *
     * @return the color of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * A function that sets the color of the geometry
     *
     * @param emission the color of the geometry
     * @return this
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * abstract function to get the normal of the vector
     */
    public abstract Vector getNormal(Point point);



    /**
     * The material of the geometry
     */
    private Material material = new Material();

    /**
     * A function that returns the material of the geometry
     *
     * @return the material of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * A function that sets the material of the geometry
     *
     * @param material the material of the geometry
     * @return this
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

}
