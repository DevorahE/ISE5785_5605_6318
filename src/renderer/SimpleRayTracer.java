package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;


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


    @Override
    public Color traceRay(Ray ray) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
