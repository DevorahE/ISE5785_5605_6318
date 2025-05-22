package lighting;

import primitives.*;

/**
 * PointLight class represents a point light source in the scene.
 * It extends the Light class and implements the LightSource interface.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class PointLight extends Light  implements LightSource{

    /**
     * The position of the light in 3D space.
     */
    private final Point position;

    /**
     * The attenuation coefficients for the light.
     * kC - constant attenuation coefficient
     * kL - linear attenuation coefficient
     * kQ - quadratic attenuation coefficient
     */
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    /**
     * Constructor for the Light class.
     * Initializes the intensity of the light.
     *
     * @param intensity the intensity of the light
     * @param position the position of the light
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        double distance = p.distance(position);
        return intensity.scale(1 / (kC + kL * distance + kQ * distance * distance));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }


    /**
     * Set the constant attenuation coefficient of the light.
     */
    public PointLight setKC(double kC)
    {
        this.kC = kC;
        return this;
    }

    /**
     * Set the linear attenuation coefficient of the light.
     */
    public PointLight setKl(double kL)
    {
        this.kL = kL;
        return this;
    }

    /**
     * Set the quadratic attenuation coefficient of the light.
     */
    public PointLight setKq(double kQ)
    {
        this.kQ = kQ;
        return this;
    }
}
