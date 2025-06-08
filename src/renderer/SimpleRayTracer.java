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

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;


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

    private boolean unshaded(Intersection intersection, LightSource lightsource){
        Vector lightDirection = intersection.lightDirection.scale(-1);// from point to light source
        Vector delta = intersection.normalIntersection.scale(intersection.lightNormalDot<0? DELTA: -DELTA);
        Ray lightRay = new Ray(intersection.point.add(delta), lightDirection);
        List<Intersection> intersections = scene.geometries.calculateIntersections(lightRay, lightsource.getDistance(intersection.point));

        if (intersections == null) return true;

        for (Intersection inter : intersections) {
            if(inter.geometry.getMaterial().kT.equals(Double3.ZERO))
                return false; // obstacle before light source
        }

        return true;

    }

    /**
     * Calculate the color intensity in the scene
     * @param intersection the intersection point
     * @return the color intensity
     */
    private Color calcColor(Ray ray, Intersection intersection) {
        if (!preprocessIntersection(intersection, ray.getDirection())) return Color.BLACK;
        Color color = scene.ambientLight.getIntensity().scale(intersection.material.kA);
        color = color.add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
        return color;
    }

    /**
     * Calculate the color intensity recursively
     * @param intersection the intersection point
     * @param level the recursion level
     * @param k the reflection coefficient
     * @return the color intensity
     */
    private Color calcColor(Intersection intersection, int level, Double3 k){
        Color color = calcColorLocalEffects(intersection);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, level, k));
    }

    /**
     * Calculates the global lighting effects (reflection and refraction) at the intersection point.
     *
     * @param intersection the intersection object
     * @param level        the current recursion level
     * @param k            the contribution factor
     * @return the global lighting color contribution
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcColorGlobalEffect(constractRefractedRay(intersection),
                level, k, intersection.material.kT)
                .add(calcColorGlobalEffect(constractReflectedRay(intersection),
                        level, k, intersection.material.kR));
    }


    /**
     * Calculates the global lighting effect for a given ray.
     *
     * @param ray  the ray to trace
     * @param level the current recursion level
     * @param k    the contribution factor
     * @param kx   the material's reflection or refraction coefficient
     * @return the global lighting color contribution
     */
    private Color calcColorGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.background.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx) : Color.BLACK;
    }


//    /**
//     * Calculates the transparency factor for the intersection point.
//     * This is done by casting a shadow ray and checking for intersections with other geometries.
//     *
//     * @param intersection the intersection object
//     * @return the transparency factor as Double3
//     */
//    private Double3 transparency(Intersection intersection) {
//        Vector lightDirection = intersection.lightDirection.scale(-1);
//        Vector delta = intersection.normalIntersection.scale(intersection.rayNormalDot < 0 ? DELTA : -DELTA);
//        Ray shadowRay = new Ray(intersection.point.add(delta), lightDirection, intersection.normalIntersection);
//        List<Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);
//
//        if (shadowIntersections == null) return Double3.ONE;
//
//        Double3 ktr = Double3.ONE;
//        double lightDistance = intersection.lightSource.getDistance(intersection.point);
//
//        for (Intersection hit : shadowIntersections) {
//            if (alignZero(hit.point.distance(intersection.point) - lightDistance) < 0) {
//                ktr = ktr.product(hit.geometry.getMaterial().kT);
//                if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
//            }
//        }
//
//        return ktr;
//    }



    /**
     * Finds the closest intersection point for a given ray.
     *
     * @param ray the ray to check for intersections
     * @return the closest intersection point, or null if no intersections are found
     */
    private Intersection findClosestIntersection(Ray ray) {
        return ray.findClosestIntersection(scene.geometries.calculateIntersections(ray));
    }


    /**
     * Constructs a refracted ray for the given intersection point.
     *
     * @param intersection the intersection object
     * @return the refracted ray
     */
    private Ray constractRefractedRay(Intersection intersection) {
//        return new Ray(intersection.point, intersection.directionRay, intersection.geometry.getNormal(intersection.point));
        return new Ray(intersection.point, intersection.directionRay, intersection.normalIntersection);
    }



    /**
     * The RayTests class contains unit tests for the {@link primitives.Ray} class.
     * It tests the functionality of methods such as {@code getPoint} and {@code findClosestPoint}.
     * The tests cover both equivalence partitions and boundary value cases.
     */
//    private Ray constractReflectedRay(Intersection intersection) {
//        Vector v = intersection.directionRay;
//        Vector n = intersection.geometry.getNormal(intersection.point);
//        double vn = v.dotProduct(n);
//        return new Ray(intersection.point, v.subtract(n.scale(2 * vn)), n);
//    }

    private Ray constractReflectedRay(Intersection intersection) {
        Vector v = intersection.directionRay;
        Vector n = intersection.geometry.getNormal(intersection.point);
        double vn = v.dotProduct(n);
        if (v.dotProduct(n) > 0) {
            n = n.scale(-1);
            vn = -vn; // Invert the normal and dot product if the ray is entering the surface
        }


        Vector r = v.subtract(n.scale(2 * vn)); // direction du rayon réfléchi

        Vector delta = n.scale(DELTA);
        Point movedPoint = intersection.point.add(delta);

        return new Ray(movedPoint, r, n);
    }


    @Override
    public Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection == null ? scene.background : calcColor(ray, closestIntersection);

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
        intersection.rayNormalDot = alignZero(intersection.directionRay.dotProduct(intersection.normalIntersection));
        return !isZero(intersection.rayNormalDot) && intersection.material !=null;
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
    private Color calcColorLocalEffects(Intersection intersection) {
        if (intersection == null) {
            return scene.background;
        }

        Color color = intersection.geometry.getEmission();
        Vector v = intersection.directionRay;
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;

        intersection.directionRay = v.scale(-1);
        intersection.normalIntersection = n;
        intersection.rayNormalDot = nv;;

        if (scene.lights == null || scene.lights.isEmpty()) {
            return color;
        }

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) continue;

            if (intersection.lightNormalDot * nv > 0 && unshaded(intersection, lightSource)) {
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
