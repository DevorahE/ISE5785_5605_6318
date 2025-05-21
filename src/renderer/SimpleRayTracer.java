package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable. Intersection;


import java.util.List;

import static primitives.Util.alignZero;


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
     * @param intersection the intersection point
     * @return the color intensity
     */
    private Color calcColor(Intersection intersection)
    {
        return scene.ambientLight.getIntensity()
                .scale(intersection.geometry.getMaterial().kA)
                .add(intersection.geometry.getEmission());
    }



    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? scene.background
                : calcColor(ray.findClosestIntersection(intersections));

    }

    /**
     * Preprocess the intersection to calculate the normal and ray-normal dot product
     * @param intersection the intersection point
     * @param directionRay the direction of the ray
     * @return true if the intersection is valid, false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector directionRay) {
        intersection.directionRay = directionRay;
        intersection.normalIntersection = intersection.geometry.getNormal(intersection.point);
        intersection.rayNormalDot = alignZero(intersection.directionRay.dotProduct(intersection.normalIntersection));
        return intersection.rayNormalDot != 0;
    }


    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.lightSource = light;
        intersection.lightDirection = light.getL(intersection.point);
        intersection.lightNormalDot = alignZero(intersection.lightDirection.dotProduct(intersection.normalIntersection));
        return intersection.lightNormalDot * intersection.rayNormalDot > 0;
    }

    private Color calcColorLocalEffects(Intersection intersection){
        return null;
    }

    private Double3 calcSpecular(Intersection intersection)
    {
        return null;
    }

    private Double3 calcDiffusive(Intersection intersection)
    {
        return null;
    }

}
