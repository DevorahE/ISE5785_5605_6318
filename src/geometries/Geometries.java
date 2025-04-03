package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Geometries class implements operations for several geometric bodies
 *
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Geometries implements Intersectable{

    private final List<Intersectable> geometries = new LinkedList<Intersectable>();

    public Geometries() {
    }

    /**
     * A constructor that receives a list of geometric objects and adds them to the list
     *
     * @param geometries
     */
    public Geometries(Intersectable...geometries) {
        add(geometries);
    }

    /**
     * Add object to list
     *
     * @param geometries objects to add
     */
    public void add(Intersectable...geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * A function that receives a foundation and returns all bodies that the foundation
     *
     * @param ray
     * @return a list of points intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {

            List<Point> intersections = null;
            for(Intersectable geo : geometries)
            {
                List<Point> geoPoints=geo.findIntersections(ray);
                if(geoPoints!=null)
                {
                    if(intersections==null)
                        intersections=new LinkedList<>();
                    intersections.addAll(geoPoints);
                }
            }
            return intersections;
    }

}
