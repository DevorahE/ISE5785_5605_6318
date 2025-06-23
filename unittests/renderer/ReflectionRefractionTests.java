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

    /**
     * Test to create a scene with multiple spheres and triangles
     * This scene includes spheres with different colors and materials,
     * triangles, and a plane.
     */
    @Test
    void bubbleTriangleImage() {
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
    /**
     * Tests to demonstrate camera transformations at different angles and distances
     * Uses the BubbleTriangleImage scene as a basis to demonstrate transformation capabilities
     */

    /**
     * Aerial view - elevated camera looking down
     */
    @Test
    void bubbleTriangleImage_AerialView() {
        bubbleTriangleImage();

        cameraBuilder
                .setLocation(new Point(0, 800, 0))
                .setDirection(new Point(0, -200, -200), new Vector(0, 0, -1))
                .setVpDistance(800).setVpSize(1500, 1500)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("BubbleTriangle_Aerial");
    }

    /**
     * Side view - camera moved on the X axis
     */
    @Test
    void bubbleTriangleImage_SideView() {
        bubbleTriangleImage();

        cameraBuilder
                .setLocation(new Point(1200, 0, 200))
                .setDirection(new Point(0, -200, -200), Vector.AXIS_Y)
                .setVpDistance(1000).setVpSize(1500, 1500)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("BubbleTriangle_Side");
    }

    /**
     * Distant diagonal view
     */
    @Test
    void bubbleTriangleImage_DiagonalView() {
        bubbleTriangleImage();

        cameraBuilder
                .setLocation(new Point(800, 600, 1500))
                .setDirection(new Point(0, -200, -200), new Vector(0, 1, 0))
                .setVpDistance(1200).setVpSize(1500, 1500)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("BubbleTriangle_Diagonal");
    }

    /**
     * Close-up view with rotation - focus on the central spheres
     */
    @Test
    void bubbleTriangleImage_CloseRotated() {
        bubbleTriangleImage();

        cameraBuilder
                .setLocation(new Point(0, 200, 600))
                .setDirection(new Point(0, -100, -200), Vector.AXIS_Y)
                .setVpDistance(500).setVpSize(800, 800)
                .setResolution(500, 500)
                .rotateZ(15)
                .build()
                .renderImage()
                .writeToImage("BubbleTriangle_CloseRotated");
    }

    /**
     * Low angle view - low camera looking up
     */
    @Test
    void bubbleTriangleImage_LowAngle() {
        bubbleTriangleImage();

        cameraBuilder
                .setLocation(new Point(0, -400, 800))
                .setDirection(new Point(0, 0, -200), new Vector(0, 1, 0))
                .setVpDistance(800).setVpSize(1500, 1500)
                .setResolution(500, 500)
                .build()
                .renderImage()
                .writeToImage("BubbleTriangle_LowAngle");
    }
/// //////////////////////////////////////////////////////

@Test
void bubbleTriangleImage_10Pictures() {
    bubbleTriangleImage();

    // Define the center point of the scene (what the camera should orbit around)
    Point sceneCenter = new Point(0, 0, 0);
    cameraBuilder
            // Set initial camera position before orbiting
            .setLocation(new Point(0, 200, 600))

            // Set viewport properties
            .setVpDistance(500)
            .setVpSize(800, 800)
            .setResolution(500, 500);

    // Generate 10 images with camera orbiting around the scene
    for (int i = 0; i < 10; i++) {
        // Calculate orbit angle: start at 15° and increment by 36° each step
        // This creates a complete 360° rotation over 10 images (36° × 10 = 360°)
        double orbitAngle = 15 + i * 36;


            cameraBuilder
                // Make camera orbit around scene center at calculated angle
                // Using Y-axis for horizontal circular motion
                .orbitAround(sceneCenter, orbitAngle, Vector.AXIS_Y)

                // Build camera, render and save image
                .build()
                .renderImage()
                .writeToImage("BubbleTriangle_Orbit" + i);
    }
}

}