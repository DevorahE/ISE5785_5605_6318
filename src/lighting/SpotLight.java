package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

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

    public PointLight setKC(double kC)
    {
        return super.setKC(kC);
    }

    public PointLight setKL(double kL)
    {
        return super.setKL(kL);
    }

    public PointLight setKQ(double kQ)
    {
        return super.setKQ(kQ);
    }

}
