package primitives;


/**
 * The Material class represents the material properties of a 3D object.
 * It contains the diffuse coefficient (kA) of the material, which determines
 * how much light is reflected by the surface.
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Material {

    /**
     * The diffuse coefficient of the material: 1 1 1
     */
    public Double3 kA = Double3.ONE;

    /**
     * Set the diffuse coefficient of the material with double3
     */
    public Material setkA(Double3 kA) {
        this.kA = kA;
        return this;
    }


    /**
     * Set the diffuse coefficient of the material with double
     */
    public Material setkA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

}
