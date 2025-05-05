package lighting;

import primitives.Color;

/**
 * AmbientLight class represents the ambient light in the scene
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class AmbientLight {

    private final Color intensity;

    /**
     * NONE is a constant for no ambient light
     */
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK);

    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    public Color getIntensity() {
        return intensity;
    }

}
