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
public class Geometries extends Intersectable{

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


    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Intersection> geometryIntersections = geometry.calculateIntersectionsHelper(ray, maxDistance);
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(geometryIntersections);
            }
        }
        return intersections;
    }
}
