package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import org.junit.jupiter.api.Test;
import renderer.sampling.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;


public class Minip2Tests {

    private final Scene scene = new Scene("Test scene").setSamplingPattern(TargetArea.SamplingPattern.JITTERED);

    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);



    @Test
    void diamondImageLuxuryShowcase() {
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

        // Points de base du diamant (légèrement surélevé)
        Point center = new Point(0, 0, -290);
        Point topCenter = new Point(0, -150 + 50 + 170, -290);
        Point bottomCenter = new Point(0, -170, -340);

        List<Triangle> triangles = new ArrayList<>();

        // === CRÉATION DU DIAMANT (code existant) ===
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

        // Pavillon du diamant
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

        // === CRÉATION DU PIÉDESTAL ÉLÉGANT ===
        Material pedestalMaterial = new Material()
                .setKD(0.3).setKS(0.7).setShininess(100)
                .setKr(new Double3(0.15, 0.15, 0.2));

        Material pedestalTopMaterial = new Material()
                .setKD(0.2).setKS(0.8).setShininess(200)
                .setKr(new Double3(0.3, 0.3, 0.35));

        // Base du piédestal (cylindre octogonal)
        double pedestalHeight = -170;
        double pedestalRadius = 200;
        int pedestalSides = 8;
        double pedestalAngleStep = 2 * Math.PI / pedestalSides;

        // Dessus du piédestal
        Point pedestalCenter = new Point(0, pedestalHeight, -290);
        for (int i = 0; i < pedestalSides; i++) {
            double angle1 = i * pedestalAngleStep;
            double angle2 = (i + 1) * pedestalAngleStep;

            Point p1 = new Point(
                    pedestalRadius * Math.cos(angle1),
                    pedestalHeight,
                    -290 + pedestalRadius * Math.sin(angle1)
            );
            Point p2 = new Point(
                    pedestalRadius * Math.cos(angle2),
                    pedestalHeight,
                    -290 + pedestalRadius * Math.sin(angle2)
            );

            Triangle pedestalTop = new Triangle(pedestalCenter, p1, p2);
            pedestalTop.setMaterial(pedestalTopMaterial);
            triangles.add(pedestalTop);
        }

        // Côtés du piédestal
        double pedestalBottomHeight = -280;
        for (int i = 0; i < pedestalSides; i++) {
            double angle1 = i * pedestalAngleStep;
            double angle2 = (i + 1) * pedestalAngleStep;

            Point p1Top = new Point(
                    pedestalRadius * Math.cos(angle1),
                    pedestalHeight,
                    -290 + pedestalRadius * Math.sin(angle1)
            );
            Point p2Top = new Point(
                    pedestalRadius * Math.cos(angle2),
                    pedestalHeight,
                    -290 + pedestalRadius * Math.sin(angle2)
            );
            Point p1Bottom = new Point(
                    pedestalRadius * Math.cos(angle1),
                    pedestalBottomHeight,
                    -290 + pedestalRadius * Math.sin(angle1)
            );
            Point p2Bottom = new Point(
                    pedestalRadius * Math.cos(angle2),
                    pedestalBottomHeight,
                    -290 + pedestalRadius * Math.sin(angle2)
            );

            Triangle side1 = new Triangle(p1Top, p1Bottom, p2Top);
            Triangle side2 = new Triangle(p2Top, p1Bottom, p2Bottom);

            side1.setMaterial(pedestalMaterial);
            side2.setMaterial(pedestalMaterial);
            triangles.add(side1);
            triangles.add(side2);
        }

//        // === COLONNES DÉCORATIVES ===
//        Material columnMaterial = new Material()
//                .setKD(0.4).setKS(0.6).setShininess(80)
//                .setKr(new Double3(0.1, 0.1, 0.15));
//
//        // Positions des colonnes
//        Point[] columnPositions = {
//                new Point(-400, -280, -450),
//                new Point(400, -280, -450),
//                new Point(-300, -280, -550),
//                new Point(300, -280, -550)
//        };
//
//        for (Point pos : columnPositions) {
//            // Colonne cylindrique simplifiée (6 faces)
//            double columnRadius = 30;
//            double columnHeight = 200;
//            int columnSides = 6;
//            double columnAngleStep = 2 * Math.PI / columnSides;
//
//            for (int i = 0; i < columnSides; i++) {
//                double angle1 = i * columnAngleStep;
//                double angle2 = (i + 1) * columnAngleStep;
//
//                Point p1Top = new Point(
//                        pos.getX() + columnRadius * Math.cos(angle1),
//                        pos.getY() + columnHeight,
//                        pos.getZ() + columnRadius * Math.sin(angle1)
//                );
//                Point p2Top = new Point(
//                        pos.getX() + columnRadius * Math.cos(angle2),
//                        pos.getY() + columnHeight,
//                        pos.getZ() + columnRadius * Math.sin(angle2)
//                );
//                Point p1Bottom = new Point(
//                        pos.() + columnRadius * Math.cos(angle1),
//                        pos.getY(),
//                        pos.getZ() + columnRadius * Math.sin(angle1)
//                );
//                Point p2Bottom = new Point(
//                        pos.getX() + columnRadius * Math.cos(angle2),
//                        pos.getY(),
//                        pos.getZ() + columnRadius * Math.sin(angle2)
//                );
//
//                Triangle columnSide1 = new Triangle(p1Top, p1Bottom, p2Top);
//                Triangle columnSide2 = new Triangle(p2Top, p1Bottom, p2Bottom);
//
//                columnSide1.setMaterial(columnMaterial);
//                columnSide2.setMaterial(columnMaterial);
//                triangles.add(columnSide1);
//                triangles.add(columnSide2);
//            }
//        }

        // === FOND ET SOL AMÉLIORÉS ===
        // Mur arrière avec dégradé
        Triangle backgroundPlane1 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(1000, -400, -700),
                new Point(1000, 600, -700)
        );
        Triangle backgroundPlane2 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(1000, 600, -700),
                new Point(-1000, 600, -700)
        );

        Material backgroundMaterial = new Material()
                .setKD(0.6).setKS(0.2).setShininess(20)
                .setKr(new Double3(0.02, 0.03, 0.05));

        backgroundPlane1
                .setEmission(new Color(2, 4, 8))
                .setMaterial(backgroundMaterial);
        backgroundPlane2
                .setEmission(new Color(2, 4, 8))
                .setMaterial(backgroundMaterial);

        // Sol en marbre avec motifs
        Material marbleFloor = new Material()
                .setKD(0.3).setKS(0.7).setShininess(150)
                .setKr(new Double3(0.4, 0.4, 0.45));

        // Sol principal
        Triangle floor1 = new Triangle(
                new Point(-800, -280, -100),
                new Point(800, -280, -100),
                new Point(800, -280, -700)
        );
        Triangle floor2 = new Triangle(
                new Point(-800, -280, -100),
                new Point(800, -280, -700),
                new Point(-800, -280, -700)
        );

        floor1.setMaterial(marbleFloor);
        floor2.setMaterial(marbleFloor);

        // Ajout de détails au sol - cercles concentriques
        Material accentFloor = new Material()
                .setKD(0.2).setKS(0.8).setShininess(200)
                .setKr(new Double3(0.5, 0.5, 0.6));

        // Cercle décoratif autour du piédestal
        int circleSegments = 12;
        double innerRadius = 250;
        double outerRadius = 280;
        double circleAngleStep = 2 * Math.PI / circleSegments;

        for (int i = 0; i < circleSegments; i++) {
            double angle1 = i * circleAngleStep;
            double angle2 = (i + 1) * circleAngleStep;

            Point p1Inner = new Point(
                    innerRadius * Math.cos(angle1),
                    -279, // Légèrement au-dessus du sol
                    -290 + innerRadius * Math.sin(angle1)
            );
            Point p2Inner = new Point(
                    innerRadius * Math.cos(angle2),
                    -279,
                    -290 + innerRadius * Math.sin(angle2)
            );
            Point p1Outer = new Point(
                    outerRadius * Math.cos(angle1),
                    -279,
                    -290 + outerRadius * Math.sin(angle1)
            );
            Point p2Outer = new Point(
                    outerRadius * Math.cos(angle2),
                    -279,
                    -290 + outerRadius * Math.sin(angle2)
            );

            Triangle ring1 = new Triangle(p1Inner, p1Outer, p2Inner);
            Triangle ring2 = new Triangle(p2Inner, p1Outer, p2Outer);

            ring1.setMaterial(accentFloor);
            ring2.setMaterial(accentFloor);
            triangles.add(ring1);
            triangles.add(ring2);
        }

        // Ajout de tous les triangles à la scène
        Triangle[] triangleArray = triangles.toArray(new Triangle[0]);
        scene.geometries.add(triangleArray);
        scene.geometries.add(backgroundPlane1, backgroundPlane2, floor1, floor2);

        // === ÉCLAIRAGE CINÉMATOGRAPHIQUE ===
        scene.setAmbientLight(new AmbientLight(new Color(5, 8, 12)));

        // Lumière principale dramatique
        scene.lights.add(new SpotLight(new Color(255, 240, 220), new Point(0, 400, 150), new Vector(0, -1, -0.8))
                .setKl(0.00005).setKq(0.0000005));

        // Éclairage d'accentuation du diamant
        scene.lights.add(new SpotLight(new Color(200, 220, 255), new Point(-150, 250, 100), new Vector(0.5, -0.8, -0.6))
                .setKl(0.0002).setKq(0.000002));

        scene.lights.add(new SpotLight(new Color(255, 200, 180), new Point(150, 250, 100), new Vector(-0.5, -0.8, -0.6))
                .setKl(0.0002).setKq(0.000002));

        // Éclairage des colonnes
        scene.lights.add(new SpotLight(new Color(120, 140, 180), new Point(-400, 100, -300), new Vector(0, -0.5, -0.3))
                .setKl(0.001).setKq(0.00001));

        scene.lights.add(new SpotLight(new Color(120, 140, 180), new Point(400, 100, -300), new Vector(0, -0.5, -0.3))
                .setKl(0.001).setKq(0.00001));

        // Lumière d'ambiance générale
        scene.lights.add(new DirectionalLight(new Color(15, 20, 30), new Vector(0, -0.2, -1)));

        // === RENDU HAUTE QUALITÉ ===
        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .setMultithreading(4)
                .build()
                .renderImage()
                .writeToImage("DiamondImage-LuxuryShowcase");
    }
}
