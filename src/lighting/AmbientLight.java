package lighting;

import primitives.Color;

/**
 * AmbientLight class represents the ambient light in the scene
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class AmbientLight {

    /**
     * The intensity of the ambient light.
     */
    private final Color intensity;


    /**
     * A constant representing the absence of ambient light.
     */
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK);


    /**
     * Constructor for the AmbientLight class.
     * Initializes the ambient light with the specified intensity.
     *
     * @param intensity the intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }


    /**
     * Getter for the intensity of the ambient light.
     *
     * @return the intensity of the ambient light
     */
    public Color getIntensity() {
        return intensity;
    }

}
