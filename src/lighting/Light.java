package lighting;

import primitives.Color;

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
