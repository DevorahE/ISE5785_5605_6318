package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import primitives.*;
import org.junit.jupiter.api.Test;
import scene.Scene;
import targetAreas.TargetArea;

import java.util.ArrayList;
import java.util.List;

public class Minip1Tests {
    private final Scene scene = new Scene("Test scene").setSamplingPattern(TargetArea.SamplingPattern.JITTERED);

    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);





    @Test
    void diamondImage2Fixed() {
        Material diamondMaterial = new Material()
                .setKD(0.1).setKS(0.9).setShininess(300)
                .setKt(new Double3(0.92, 0.95, 0.98))
                .setKr(new Double3(0.4, 0.5, 0.6));

        Material diamondMaterialBright = new Material()
                .setKD(0.08).setKS(0.92).setShininess(400)
                .setKt(new Double3(0.94, 0.96, 0.99))
                .setKr(new Double3(0.45, 0.55, 0.65));

        Material diamondMaterialDark = new Material()
                .setKD(0.12).setKS(0.88).setShininess(250)
                .setKt(new Double3(0.88, 0.92, 0.96))
                .setKr(new Double3(0.35, 0.45, 0.55));


        Point center = new Point(0, 0, -290);
        Point topCenter = new Point(0, -150 + 50 + 170, -290);
        Point bottomCenter = new Point(0, -170, -340);

        List<Triangle> triangles = new ArrayList<>();

        double angleStep = 2 * Math.PI / 24;
        double radiusMiddle = 100;
        double heightMiddle = 60;

        for (int i = 0; i < 24; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            Point p1Mid = new Point(
                    radiusMiddle * Math.cos(angle1),
                    heightMiddle,
                    -270 + radiusMiddle * Math.sin(angle1) * 0.2
            );
            Point p2Mid = new Point(
                    radiusMiddle * Math.cos(angle2),
                    heightMiddle,
                    -270 + radiusMiddle * Math.sin(angle2) * 0.2
            );

            Triangle triangle = new Triangle(center, p1Mid, p2Mid);

            triangle.setMaterial(i % 3 == 0 ? diamondMaterialBright : diamondMaterial);
            triangles.add(triangle);
        }

        double radiusLarge = 160;
        double heightLow = -20;

        for (int i = 0; i < 24; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            Point p1Mid = new Point(
                    radiusMiddle * Math.cos(angle1),
                    heightMiddle,
                    -270 + radiusMiddle * Math.sin(angle1) * 0.2
            );
            Point p2Mid = new Point(
                    radiusMiddle * Math.cos(angle2),
                    heightMiddle,
                    -270 + radiusMiddle * Math.sin(angle2) * 0.2
            );

            Point p1Large = new Point(
                    radiusLarge * Math.cos(angle1),
                    heightLow,
                    -260 + radiusLarge * Math.sin(angle1) * 0.15
            );
            Point p2Large = new Point(
                    radiusLarge * Math.cos(angle2),
                    heightLow,
                    -260 + radiusLarge * Math.sin(angle2) * 0.15
            );

            Triangle triangle1 = new Triangle(p1Mid, p1Large, p2Mid);
            triangle1.setMaterial(i % 2 == 0 ? diamondMaterial : diamondMaterialDark);
            triangles.add(triangle1);

            Triangle triangle2 = new Triangle(p2Mid, p1Large, p2Large);
            triangle2.setMaterial(diamondMaterialDark);
            triangles.add(triangle2);
        }

        for (int i = 0; i < 24; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            Point p1Large = new Point(
                    radiusLarge * Math.cos(angle1),
                    heightLow,
                    -260 + radiusLarge * Math.sin(angle1) * 0.15
            );
            Point p2Large = new Point(
                    radiusLarge * Math.cos(angle2),
                    heightLow,
                    -260 + radiusLarge * Math.sin(angle2) * 0.15
            );

            Triangle triangle = new Triangle(p1Large, bottomCenter, p2Large);

            triangle.setMaterial(diamondMaterialDark);
            triangles.add(triangle);
        }

        Triangle backgroundPlane1 = new Triangle(
                new Point(-800, -400, -600),
                new Point(800, -400, -600),
                new Point(800, 400, -600)
        );
        Triangle backgroundPlane2 = new Triangle(
                new Point(-800, -400, -600),
                new Point(800, 400, -600),
                new Point(-800, 400, -600)
        );

        Material backgroundMaterial = new Material()
                .setKD(0.8).setKS(0.1).setShininess(10)
                .setKr(new Double3(0.05, 0.05, 0.08));

        backgroundPlane1
                .setEmission(new Color(5, 8, 15))
                .setMaterial(backgroundMaterial);
        backgroundPlane2
                .setEmission(new Color(5, 8, 15))
                .setMaterial(backgroundMaterial);

        Triangle floor1 = new Triangle(
                new Point(-500, -300, -100),
                new Point(500, -300, -100),
                new Point(500, -300, -500)
        );
        Triangle floor2 = new Triangle(
                new Point(-500, -300, -100),
                new Point(500, -300, -500),
                new Point(-500, -300, -500)
        );

        Material floorMaterial = new Material()
                .setKD(0.4).setKS(0.3).setShininess(80)
                .setKr(new Double3(0.2, 0.25, 0.3));

        floor1
                .setEmission(new Color(3, 5, 10))
                .setMaterial(floorMaterial);
        floor2
                .setEmission(new Color(3, 5, 10))
                .setMaterial(floorMaterial);

        Triangle[] triangleArray = triangles.toArray(new Triangle[0]);
        scene.geometries.add(triangleArray);
        scene.geometries.add(backgroundPlane1, backgroundPlane2, floor1, floor2);

        scene.setAmbientLight(new AmbientLight(new Color(3, 5, 8)));

        scene.lights.add(new SpotLight(new Color(255, 255, 255), new Point(0, 300, 200), new Vector(0, -1, -1))
                .setKl(0.0001).setKq(0.000001));

        scene.lights.add(new SpotLight(new Color(120, 130, 150), new Point(-200, 200, 100), new Vector(1, -0.5, -0.8))
                .setKl(0.0008).setKq(0.000008));

        scene.lights.add(new SpotLight(new Color(150, 140, 120), new Point(200, 200, 100), new Vector(-1, -0.5, -0.8))
                .setKl(0.0008).setKq(0.000008));

        scene.lights.add(new DirectionalLight(new Color(10, 15, 25), new Vector(0, -0.3, -1)));

        cameraBuilder
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(500, 500)
                .setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("DiamondImage-WithoutImprovedEffects2");

        cameraBuilder
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
                .setVpDistance(300)
                .setVpSize(600, 600)
                .setResolution(500, 500)
                .setNumOfRaysAA(30)
                .setNumOfRaysDOF(30)
                .setApertureWindow(20, 40)
                .setDistanceFocalPlane(390)  //
                .setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("DiamondImage-WithImprovedEffects2");
    }





}
