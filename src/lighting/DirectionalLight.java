package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * DirectionalLight class represents a directional light source in the scene.
 * It extends the Light class and implements the LightSource interface.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class DirectionalLight extends Light implements LightSource{
    /**
     * The direction of the light in 3D space.
     */
    private final Vector direction;

    /**
     * Constructor for the Light class.
     * Initializes the intensity of the light.
     *
     * @param intensity the intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }
}
