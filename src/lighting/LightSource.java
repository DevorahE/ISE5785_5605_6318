package lighting;
import primitives.*;

/**
 * The LightSource interface represents a light source in a 3D scene.
 * It provides methods to get the intensity of the light at a specific point
 * and the direction of the light.
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public interface LightSource {

    /**
     * Returns the intensity of the light at a given point.
     *
     * @param p the point at which to get the light intensity
     * @return the color representing the intensity of the light at the point
     */
    Color getIntensity(Point p);

    /**
     * Returns the direction of the light at a given point.
     *
     * @param p the point at which to get the light direction
     * @return the vector representing the direction of the light at the point
     */
    Vector getL(Point p);

    /**
     * Returns the distance from the light source to a given point.
     *
     * @param point the point at which to get the distance from the light source
     * @return the distance from the light source to the point
     */
    double getDistance(Point point);

}
