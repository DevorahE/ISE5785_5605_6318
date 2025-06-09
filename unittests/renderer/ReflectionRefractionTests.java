package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
    /**
     * Default constructor to satisfy JavaDoc generator
     */
    ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    void twoSpheres() {
        scene.geometries.add( //
                new Sphere(50d, new Point(0, 0, -50)).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKt(0.3)), //
                new Sphere(25d, new Point(0, 0, -50)).setEmission(new Color(RED)) //
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
        scene.lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(150, 150) //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("refractionTwoSpheres");
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    void twoSpheresOnMirrors() {
        scene.geometries.add( //
                new Sphere(400d, new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100)) //
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                                .setKt(new Double3(0.5, 0, 0))), //
                new Sphere(200d, new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20)) //
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                        new Point(670, 670, 3000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(1)), //
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                        new Point(-1500, -1500, -2000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        cameraBuilder
                .setLocation(new Point(0, 0, 10000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(10000).setVpSize(2500, 2500) //
                .setResolution(500, 500) //
                .build() //
                .renderImage() //
                .writeToImage("reflectionTwoSpheresMirrored");
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Sphere(30d, new Point(60, 50, -50)).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKt(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKl(4E-5).setKq(2E-7));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000)) //
                .setDirection(Point.ZERO, Vector.AXIS_Y) //
                .setVpDistance(1000).setVpSize(200, 200) //
                .setResolution(600, 600) //
                .build() //
                .renderImage() //
                .writeToImage("refractionShadow");
    }


    /***********************************************************************************************/

    @Test
    void diamondFlatTopPolygon() {
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 20)));


        Point A = new Point(-30, 5, 0);
        Point B = new Point(-10, 10, 0);
        Point C = new Point(10, 10, 0);
        Point D = new Point(30, 5, 0);
        Point E = new Point(30, -5, 0);
        Point F = new Point(10, -10, 0);
        Point G = new Point(-10, -10, 0);
        Point H = new Point(-30, -5, 0);

        Polygon base = new Polygon(A, B, C, D, E, F, G, H);

        base.setEmission(new Color(150, 250, 255));
        base.setMaterial(new Material().setKt(0.7).setKr(0.1).setShininess(300));

        scene.geometries.add(base);

        // Caméra pour bien voir la forme
        cameraBuilder
                .setLocation(new Point(0, 0, 300))
                .setDirection(Point.ZERO, Vector.AXIS_Y)//
                .setVpDistance(300)
                .setVpSize(200, 200)
                .setResolution(600, 600)
                .build()
                .renderImage()
                .writeToImage("diamondFlatTopPolygon");
    }

//    @Test
//
//
//
//    void diamondScene() {
//        scene.geometries.add(
//                // Diamant central - sphère avec haute transparence et réflexion
//                new Sphere(150d, new Point(0, 0, -200))
//                        .setEmission(new Color(20, 20, 30))
//                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.8, 0.8, 0.8))
//                                .setKr(new Double3(0.3, 0.3, 0.3))),
//
//                // Socle du diamant - cylindre/sphère aplatie
//                new Sphere(80d, new Point(0, -350, -200))
//                        .setEmission(new Color(40, 40, 40))
//                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)
//                                .setKr(new Double3(0.4, 0.4, 0.4))),
//
//                // Sphère rouge transparente à gauche
//                new Sphere(100d, new Point(-300, -50, -100))
//                        .setEmission(new Color(50, 10, 10))
//                        .setMaterial(new Material().setKD(0.2).setKS(0.5).setShininess(150)
//                                .setKt(new Double3(0.6, 0.1, 0.1))),
//
//                // Sphère bleue transparente à droite
//                new Sphere(100d, new Point(300, -50, -100))
//                        .setEmission(new Color(10, 10, 50))
//                        .setMaterial(new Material().setKD(0.2).setKS(0.5).setShininess(150)
//                                .setKt(new Double3(0.1, 0.1, 0.6))),
//
//                // Miroir de fond - triangle large
//                new Triangle(new Point(1000, -500, -800), new Point(-1000, -500, -800),
//                        new Point(0, 800, -800))
//                        .setEmission(new Color(15, 15, 15))
//                        .setMaterial(new Material().setKr(new Double3(0.8, 0.8, 0.8))),
//
//                // Sol réfléchissant - triangle
//                new Triangle(new Point(1000, -400, -1000), new Point(-1000, -400, -1000),
//                        new Point(0, -400, 500))
//                        .setEmission(new Color(20, 20, 20))
//                        .setMaterial(new Material().setKr(new Double3(0.7, 0.7, 0.7))));
//
//        scene.setAmbientLight(new AmbientLight(new Color(30, 30, 30)));
//
//        scene.lights.add(new SpotLight(new Color(800, 800, 1000), new Point(-200, 200, 300), new Vector(1, -1, -2))
//                .setKl(0.0001).setKq(0.000001));
//
//        scene.lights.add(new DirectionalLight(new Color(200, 200, 200), new Vector(0, -1, -1)));
//
//        cameraBuilder
//                .setLocation(new Point(0, 0, 1000))
//                .setDirection(Point.ZERO, Vector.AXIS_Y)
//                .setVpDistance(1000).setVpSize(1500, 1500)
//                .setResolution(500, 500)
//                .build()
//                .renderImage()
//                .writeToImage("diamondScene");
//    }

    /**
     * Test to create a scene with multiple spheres and triangles
     * This scene includes spheres with different colors and materials,
     * triangles, and a plane.
     */
    @Test
    void BubbleTriangleImage() {
        scene.geometries.add(
                new Sphere(150d, new Point(0, 0, -200))
                        .setEmission(new Color(0, 10, 15))
                        .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(300)
                                .setKt(new Double3(0.8, 0.9, 0.9))
                                .setKr(new Double3(0.4, 0.5, 0.5))),

                new Sphere(80d, new Point(0, -350, -200))
                        .setEmission(new Color(20, 50, 60))
                        .setMaterial(new Material().setKD(0.5).setKS(0.6).setShininess(150)
                                .setKr(new Double3(0.5, 0.6, 0.6))),

                new Sphere(100d, new Point(-300, -50, -100))
                        .setEmission(new Color(40, 90, 100))
                        .setMaterial(new Material().setKD(0.2).setKS(0.7).setShininess(200)
                                .setKt(new Double3(0.6, 0.8, 0.8))),

                new Sphere(100d, new Point(300, -50, -100))
                        .setEmission(new Color(20, 70, 85))
                        .setMaterial(new Material().setKD(0.2).setKS(0.7).setShininess(200)
                                .setKt(new Double3(0.4, 0.7, 0.8))),

                new Sphere(150d, new Point(0, -700, -200))
                        .setEmission(new Color(15, 40, 45))
                        .setMaterial(new Material().setKD(0.1).setKS(0.4).setShininess(150)
                                .setKt(new Double3(0.4, 0.5, 0.5))
                                .setKr(new Double3(0.2, 0.3, 0.3))),

                new Sphere(100d, new Point(-300, -600, -100))
                        .setEmission(new Color(20, 45, 50))
                        .setMaterial(new Material().setKD(0.2).setKS(0.4).setShininess(100)
                                .setKt(new Double3(0.3, 0.4, 0.4))),

                new Sphere(100d, new Point(300, -600, -100))
                        .setEmission(new Color(10, 35, 42))
                        .setMaterial(new Material().setKD(0.2).setKS(0.4).setShininess(100)
                                .setKt(new Double3(0.2, 0.35, 0.4))),

                new Plane(new Point(0, 0, -1000), new Vector(0, 0, 1))
                        .setEmission(new Color(5, 15, 25))
                        .setMaterial(new Material().setKD(0.8).setKS(0.1)),

                new Triangle(new Point(1000, -500, -800), new Point(-1000, -500, -800),
                        new Point(0, 800, -800))
                        .setEmission(new Color(60, 30, 80))
                        .setMaterial(new Material().setKr(new Double3(0.7, 0.5, 0.8))
                                .setKD(0.1).setKS(0.3).setShininess(100)),

                new Plane(new Point(0, -350, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(25, 15, 35))
                        .setMaterial(new Material().setKr(new Double3(0.4, 0.3, 0.5))
                                .setKD(0.2).setKS(0.4).setShininess(80)));

        scene.setAmbientLight(new AmbientLight(new Color(10, 10, 15)));

        scene.lights.add(new SpotLight(new Color(300, 250, 200), new Point(-200, 200, 300), new Vector(1, -1, -2))
                .setKl(0.0001).setKq(0.000001));

        scene.lights.add(new DirectionalLight(new Color(80, 120, 150), new Vector(0, -1, -1)));

        scene.lights.add(new SpotLight(new Color(100, 50, 150), new Point(400, 100, 200), new Vector(-1, 0, -1))
                .setKl(0.0002).setKq(0.000002));

        scene.lights.add(new PointLight(new Color(50, 150, 180), new Point(0, 300, -600))
                .setKl(0.0001).setKq(0.000001));

        cameraBuilder
                .setLocation(new Point(0, 0, 1000))
                .setDirection(Point.ZERO, Vector.AXIS_Y)
                .setVpDistance(1000).setVpSize(1500, 1500)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("BubbleTriangleImage");
    }
}