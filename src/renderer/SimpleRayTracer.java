package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;


/**
 * SimpleRayTracer class is a basic implementation of a ray tracer.
 * It extends the RayTracerBase class and provides a method to trace rays in a scene.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class SimpleRayTracer extends RayTracerBase{


    /**
     * Constructor for SimpleRayTracer
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Calculate the color intensity in the scene
     * @param
     * @return the color intensity
     */
    private Color calcColor(Point point)
    {
        return scene.ambientLight.getIntensity();
    }



    /**
     * Trace a ray in the scene
     * @param ray the ray to trace
     * @return the color intensity in the point
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> pointsList= scene.geometries.findIntersections(ray);
        if(pointsList==null)
            return scene.background;
        else
        {
            Point closestPoint=ray.findClosestPoint(pointsList);
            return calcColor(closestPoint);
        }

    }
}
