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
     * kD is the diffuse factor
     */
    public Double3 kD = Double3.ZERO ;

    /**
     * kS is the specular factor
     */
    public Double3 kS = Double3.ZERO;

    /**
     * concentration level of the lightning
     */
    public int nSh = 0;


    /**
     * kt is the transparency factor
     */
    public Double3 kT = Double3.ZERO;

    /**
     * kr is the reflection factor
     */
    public Double3 kR = Double3.ZERO;


    /**
     * Set the transparency factor of the material with double3
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }
    /**
     * Set the transparency factor of the material with double
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Set the reflection factor of the material with double
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Set the reflection factor of the material with double3
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }



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


    /**
     * Set the diffuse factor of the material with double3
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Set the diffuse factor of the material with double
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }


    /**
     * Set the specular factor of the material with double3
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }


    /**
     * Set the specular coefficient of the material with double
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Set the concentration level of the lighting
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }

}
