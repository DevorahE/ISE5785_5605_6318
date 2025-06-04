package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable. Intersection;


import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * SimpleRayTracer class is a basic implementation of a ray tracer.
 * It extends the RayTracerBase class and provides a method to trace rays in a scene.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class SimpleRayTracer extends RayTracerBase{

    /**
     * A small delta value used to move beginning of the rays
     */
    private static final double DELTA = 0.1;


    /**
     * Constructor for SimpleRayTracer
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }


//    private boolean unshaded(Intersection intersection)
//    {
//        Vector pointToLight = intersection.lightDirection.scale(-1); // from point to light source
//        Vector deltaVector = intersection.normalIntersection.scale(intersection.lightNormalDot < 0 ? DELTA : - DELTA);
//        Point point = intersection.point.add(deltaVector);
//        Ray shadowRay = new Ray(point, pointToLight);
//        var intersections = scene.geometries.findIntersections(shadowRay);
//        if (intersections == null) return true;
//        double lightDistance = intersection.lightDirection.length();
//
//        for (Point p : intersections) {
//            if (p.distance(point) < lightDistance) {
//                return false; // obstacle before light source
//            }
//        }
//        return true;
//    }


    private boolean unshaded(Intersection intersection){
        Vector lightDirection = intersection.lightDirection.scale(-1); // from point to light source
        Vector delta = intersection.normalIntersection.scale(intersection.lightNormalDot<0?DELTA:-DELTA);
        Ray lightRay = new Ray(intersection.point.add(delta), lightDirection);
        List<Intersection> intersections = scene.geometries.calculateIntersections(lightRay, intersection.lightSource.getDistance(intersection.point));
        return intersections == null;
    }

    /**
     * Calculate the color intensity in the scene
     * @param intersection the intersection point
     * @return the color intensity
     */
    private Color calcColor(Ray ray, Intersection intersection) {

        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        return intersection.geometry.getEmission()
                .add(scene.ambientLight.getIntensity().scale(intersection.material.kA))
                .add(calcColorLocalEffects(intersection));

    }



    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? scene.background : calcColor(ray, ray.findClosestIntersection(intersections));

    }

    /**
     * Preprocess the intersection to calculate the normal and ray-normal dot product
     * @param intersection the intersection point
     * @param directionRay the direction of the ray
     * @return true if the intersection is valid, false otherwise
     */
    private boolean preprocessIntersection(Intersection intersection, Vector directionRay) {
        if (intersection.geometry == null) {
            return false;
        }

        intersection.directionRay = directionRay;
        intersection.normalIntersection = intersection.geometry.getNormal(intersection.point);
        intersection.rayNormalDot = intersection.normalIntersection.dotProduct(directionRay);
        return !isZero(intersection.rayNormalDot);
    }


    /**
     * Set the light source for the intersection
     * @param intersection the intersection point
     * @param light the light source
     * @return true if the light source is valid, false otherwise
     */
    private boolean setLightSource(Intersection intersection, LightSource light) {
        intersection.lightSource = light;
        intersection.lightDirection = light.getL(intersection.point);
        intersection.lightNormalDot = alignZero(intersection.lightDirection.dotProduct(intersection.normalIntersection));
        return intersection.lightNormalDot * intersection.rayNormalDot > 0;
    }


    /**
     * Calculate the color based on local effects
     * @param intersection the intersection point
     * @return the color intensity
     */
    private Color calcColorLocalEffects(Intersection intersection)
    {
        Vector v = intersection.directionRay;
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;

        intersection.directionRay = v.scale(-1);
        intersection.normalIntersection = n;
        intersection.rayNormalDot = nv;

        Color color = Color.BLACK;

        if (scene.lights == null || scene.lights.isEmpty()) {
            return color;
        }

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            if (intersection.lightNormalDot * nv > 0 && unshaded(intersection)) {
                Color lightIntensity = lightSource.getIntensity(intersection.point);

                Double3 diffusive = calcDiffusive(intersection);
                Double3 specular = calcSpecular(intersection);
                color = color.add(lightIntensity.scale(diffusive.add(specular)));
            }
        }

        return color;

    }

    /**
     * Calculates specular reflection component.
     * @param intersection the intersection information
     * @return the specular component as a Double3
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.normalIntersection.scale(2 * intersection.lightNormalDot)
                .subtract(intersection.lightDirection);
        double factor = Math.max(0, -intersection.directionRay.dotProduct(r));
        return intersection.material.kS.scale(Math.pow(factor, intersection.material.nSh));

    }


    /**
     * Calculates diffusive reflection component.
     * @param intersection the intersection information
     * @return the diffusive component as a Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        double factor = Math.abs(intersection.lightNormalDot);
        return intersection.material.kD.scale(factor);
    }


}
