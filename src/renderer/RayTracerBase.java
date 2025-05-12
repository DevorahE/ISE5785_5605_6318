package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;


/**
 * Abstract base class for ray tracing in a 3D scene.
 * Provides the structure for tracing rays and calculating their color.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public abstract class RayTracerBase {


    /**
     * The scene to be rendered.
     */
    protected final Scene scene;


    /**
     * Constructor for the RayTracerBase class.
     * Initializes the ray tracer with the given scene.
     *
     * @param scene the 3D scene to be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }


    /**
     * Abstract method to trace a ray and calculate its color.
     * Must be implemented by subclasses.
     *
     * @param ray the ray to be traced
     * @return the color resulting from tracing the ray
     */
    public abstract Color traceRay(Ray ray);
}
