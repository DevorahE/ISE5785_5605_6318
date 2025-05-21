package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

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
    private Double narrowBeam = 1d;


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
        this.direction = direction;
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
        this.narrowBeam = narrowBeam;
        return this;
    }
}
