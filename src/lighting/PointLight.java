package lighting;

import primitives.*;

public class PointLight extends Light  implements LightSource{
    private final Point position;
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
        double d = position.distance(p);
        double factor = kC + kL * d + kQ * d * d;
        if(Util.isZero(factor))
            return intensity.scale(Double.POSITIVE_INFINITY);

        return intensity.scale(1d/factor);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    public PointLight setKC(double kC)
    {
        this.kC = kC;
        return this;
    }

    public PointLight setKL(double kL)
    {
        this.kL = kL;
        return this;
    }

    public PointLight setKQ(double kQ)
    {
        this.kQ = kQ;
        return this;
    }
}
