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
    private final Vector direction;

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
    public PointLight setKC(double kC)
    {
        return super.setKC(kC);
    }

    @Override
    public PointLight setKL(double kL)
    {
        return super.setKL(kL);
    }

    @Override
    public PointLight setKQ(double kQ)
    {
        return super.setKQ(kQ);
    }

}
