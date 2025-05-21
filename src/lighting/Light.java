package lighting;

import primitives.Color;

/**
 * The Light class  is an abstract class that serves as a base for different types of light sources.
 * The class contains the intensity of the light, which is represented by a Color object.
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
abstract class Light {

    /**
     * The intensity of the ambient light.
     */
    protected final Color intensity;

    /**
     * Constructor for the Light class.
     * Initializes the intensity of the light.
     *
     * @param intensity the intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the light.
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
