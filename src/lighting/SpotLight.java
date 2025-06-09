package lighting;

import primitives.*;

import static java.lang.Math.pow;
import static primitives.Util.alignZero;

/**
 * SpotLight class represents a spotlight in the scene.
 * It extends the PointLight class and adds a direction to the light.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class SpotLight extends PointLight{
    /**
     * The direction of the light.
     */
    private final Vector direction;
    /**
     * The narrow beam of the light.
     */
    private double narrowBeam = 1;


    /**
     * Constructor for the Light class.
     * Initializes the intensity of the light.
     *
     * @param intensity the intensity of the light
     * @param position the position of the light
     * @param direction the direction of the light
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        Color intensity = super.getIntensity(p);
        double factor = Math.max(0, direction.dotProduct(getL(p)));
        factor = Math.pow(factor, narrowBeam);
        return intensity.scale(factor);

    }

    @Override
    public SpotLight setKC(double kC)
    {
         super.setKC(kC);
         return this;
    }

    @Override
    public SpotLight setKl(double kL)
    {
         super.setKl(kL);
         return this;
    }

    @Override
    public SpotLight setKq(double kQ)
    {
         super.setKq(kQ);
         return this;
    }

    /**
     * set the narrow beam of the light
     * @param narrowBeam the narrow beam of the light
     * @return the light source
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        if (narrowBeam < 1)
            throw new IllegalArgumentException("narrowBeam must be >= 1");
        this.narrowBeam = narrowBeam;
        return this;

    }
}
