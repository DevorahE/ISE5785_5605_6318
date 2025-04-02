package geometries;
import primitives.*;
import java.util.List;


/**
 * interface Intersectable represents a geometry object that can be
 * intersected by a ray.
 *
 * @author Devorah wajs and guila czerniewicz
 */
public interface Intersectable {

    /**
     * Finds intersection points between the intersectable object and a given ray.
     *
     * @param ray
     * @return A list of intersection points between the object and the ray.
     */

    public List<Point> findIntsersections(Ray ray);

}
