package geometries;
import primitives.*;
import java.util.List;
import java.util.Objects;


/**
 * Abstract class Intersectable represents a geometry object that can be
 * intersected by a ray.
 *
 * @author Devorah wajs and guila czerniewicz
 */
public abstract class Intersectable {

    /**
     * Finds intersection points between the intersectable object and a given ray.
     *
     * @param ray
     * @return A list of intersection points between the object and the ray.
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Finds intersection points between the intersectable object and a given ray.
     *
     * @param ray
     * @return A list of intersection points between the object and the ray.
     */
    protected  List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    /**
     * Finds intersection points between the intersectable object and a given ray.
     * Calls calculateIntersectionsHelper
     * @param ray
     * @return A list of intersection points between the object and the ray.
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
       return calculateIntersectionsHelper(ray);
    }



    /**
    * A passive data structure (PDS) representing an intersection between a ray
    * and a geometry.
     **/
    public static class Intersection {

        /**
         * The geometry that the point is on.
         */
        public final Geometry geometry;

        /**
         * The point in 3D space.
         */
        public final Point point;

        public final Material material;

        /**
         * Constructor to initialize the intersection with a geometry and a point.
         *
         * @param geometry The geometry that the point is on.
         * @param point    The point in 3D space.
         */
        public Intersection(Geometry geometry, Point point, Material material) {
            this.geometry = geometry;
            this.point = point;

            if(material == null)
                throw new IllegalArgumentException();

            this.material = material;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return (obj instanceof Intersection other)
                    && geometry == other.geometry
                    && point.equals(other.point);
        }



        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }


}
