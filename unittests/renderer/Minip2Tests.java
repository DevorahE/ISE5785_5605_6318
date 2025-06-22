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


public class Minip2Tests {

    private final Scene scene = new Scene("Test scene").setSamplingPattern(TargetArea.SamplingPattern.JITTERED);

    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);



    @Test
    //1 piedestal, murs beige foncé
    void firstdiam() {
        // === MATÉRIAUX DIAMANT AMÉLIORÉS ===
        Material diamondMaterial = new Material()
                .setKD(0.02).setKS(0.98).setShininess(1000)
                .setKt(new Double3(0.88, 0.92, 0.96))
                .setKr(new Double3(0.8, 0.85, 0.9));

        Material diamondMaterialBright = new Material()
                .setKD(0.01).setKS(0.99).setShininess(1200)
                .setKt(new Double3(0.90, 0.94, 0.98))
                .setKr(new Double3(0.85, 0.88, 0.92));

        Material diamondMaterialDark = new Material()
                .setKD(0.03).setKS(0.97).setShininess(800)
                .setKt(new Double3(0.85, 0.89, 0.94))
                .setKr(new Double3(0.75, 0.80, 0.85));

        // Points de base du diamant
        Point center = new Point(0, 0, -290);
        Point topCenter = new Point(0, -150 + 50 + 170, -290);
        Point bottomCenter = new Point(0, -170, -340);

        List<Triangle> triangles = new ArrayList<>();

        // === CRÉATION DU DIAMANT (structure identique mais matériaux améliorés) ===
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

        // === PIÉDESTAL EN VELOURS ROUGE ===
        Material pedestalMaterial = new Material()
                .setKD(0.8).setKS(0.2).setShininess(30)
                .setKr(new Double3(0.05, 0.05, 0.05));

        Material pedestalTopMaterial = new Material()
                .setKD(0.7).setKS(0.3).setShininess(50)
                .setKr(new Double3(0.1, 0.1, 0.1));

        // Base du piédestal (cylindre octogonal)
        double pedestalHeight = -170;
        double pedestalRadius = 200;
        int pedestalSides = 8;
        double pedestalAngleStep = 2 * Math.PI / pedestalSides;

        // Dessus du piédestal (velours rouge)
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
            pedestalTop.setMaterial(pedestalTopMaterial)
                    .setEmission(new Color(80, 20, 20)); // Rouge velours
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

            side1.setMaterial(pedestalMaterial)
                    .setEmission(new Color(60, 15, 15)); // Rouge plus foncé
            side2.setMaterial(pedestalMaterial)
                    .setEmission(new Color(60, 15, 15));
            triangles.add(side1);
            triangles.add(side2);
        }

        // === MURS DE LA SALLE (BEIGE LUXUEUX) ===
        Material wallMaterial = new Material()
                .setKD(0.7).setKS(0.3).setShininess(40)
                .setKr(new Double3(0.1, 0.1, 0.1));

        // Mur arrière
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

        backgroundPlane1.setMaterial(wallMaterial)
                .setEmission(new Color(45, 40, 35)); // Beige chaud
        backgroundPlane2.setMaterial(wallMaterial)
                .setEmission(new Color(45, 40, 35));

        // Murs latéraux
        Triangle leftWall1 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(-1000, 600, -700),
                new Point(-1000, 600, 300)    // Étendu vers l'avant
        );
        Triangle leftWall2 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(-1000, 600, 300),   // Étendu vers l'avant
                new Point(-1000, -400, 300)
        );

        Triangle rightWall1 = new Triangle(
                new Point(1000, -400, -700),
                new Point(1000, 600, 300),    // Étendu vers l'avant
                new Point(1000, 600, -700)
        );
        Triangle rightWall2 = new Triangle(
                new Point(1000, -400, -700),
                new Point(1000, -400, 300),   // Étendu vers l'avant
                new Point(1000, 600, 300)
        );

        leftWall1.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));
        leftWall2.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));
        rightWall1.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));
        rightWall2.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));

        // === SOL EN MARBRE CLAIR ===
        Material marbleFloor = new Material()
                .setKD(0.4).setKS(0.6).setShininess(200)
                .setKr(new Double3(0.3, 0.3, 0.3));

        Triangle floor1 = new Triangle(
                new Point(-1000, -280, 300),   // Étendu vers l'avant
                new Point(1000, -280, 300),
                new Point(1000, -280, -700)
        );
        Triangle floor2 = new Triangle(
                new Point(-1000, -280, 300),   // Étendu vers l'avant
                new Point(1000, -280, -700),
                new Point(-1000, -280, -700)
        );

        floor1.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45)); // Marbre beige
        floor2.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45));

        // === PLAFOND ===
        Material ceilingMaterial = new Material()
                .setKD(0.8).setKS(0.2).setShininess(20)
                .setKr(new Double3(0.05, 0.05, 0.05));

        Triangle ceiling1 = new Triangle(
                new Point(-1000, 600, -700),
                new Point(1000, 600, 300),    // Étendu vers l'avant
                new Point(1000, 600, -700)
        );
        Triangle ceiling2 = new Triangle(
                new Point(-1000, 600, -700),
                new Point(-1000, 600, 300),   // Étendu vers l'avant
                new Point(1000, 600, 300)
        );

        ceiling1.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));
        ceiling2.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));

        // Cercle décoratif doré autour du piédestal
        Material goldAccent = new Material()
                .setKD(0.2).setKS(0.8).setShininess(300)
                .setKr(new Double3(0.4, 0.4, 0.2));

        int circleSegments = 16;
        double innerRadius = 250;
        double outerRadius = 280;
        double circleAngleStep = 2 * Math.PI / circleSegments;

        for (int i = 0; i < circleSegments; i++) {
            double angle1 = i * circleAngleStep;
            double angle2 = (i + 1) * circleAngleStep;

            Point p1Inner = new Point(
                    innerRadius * Math.cos(angle1),
                    -279,
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

            ring1.setMaterial(goldAccent).setEmission(new Color(80, 60, 20)); // Or
            ring2.setMaterial(goldAccent).setEmission(new Color(80, 60, 20));
            triangles.add(ring1);
            triangles.add(ring2);
        }

        // Ajout de tous les triangles à la scène
        Triangle[] triangleArray = triangles.toArray(new Triangle[0]);
        scene.geometries.add(triangleArray);
        scene.geometries.add(backgroundPlane1, backgroundPlane2, floor1, floor2);
        scene.geometries.add(leftWall1, leftWall2, rightWall1, rightWall2);
        scene.geometries.add(ceiling1, ceiling2);

        // === ÉCLAIRAGE RÉALISTE ET CHALEUREUX ===
        scene.setAmbientLight(new AmbientLight(new Color(25, 20, 15))); // Ambiance chaude

        // Lumière principale du plafond (plus douce)
        scene.lights.add(new SpotLight(new Color(180, 160, 140), new Point(0, 500, -200), new Vector(0, -1, -0.3))
                .setKl(0.0001).setKq(0.000001));

        // Éclairage spécialisé pour le diamant (plus subtil)
        scene.lights.add(new SpotLight(new Color(120, 130, 150), new Point(-200, 300, 0), new Vector(0.8, -1, -0.8))
                .setKl(0.0005).setKq(0.000005));

        scene.lights.add(new SpotLight(new Color(150, 140, 120), new Point(200, 300, 0), new Vector(-0.8, -1, -0.8))
                .setKl(0.0005).setKq(0.000005));

        // Lumière d'accentuation depuis le haut
        scene.lights.add(new SpotLight(new Color(100, 110, 130), new Point(0, 400, -100), new Vector(0, -1, -0.5))
                .setKl(0.0003).setKq(0.000003));

        // Éclairage d'ambiance générale plus doux
        scene.lights.add(new DirectionalLight(new Color(20, 18, 15), new Vector(0.2, -0.5, -1)));

        // Lumières latérales pour éclairer les murs
        scene.lights.add(new SpotLight(new Color(60, 55, 50), new Point(-500, 200, -300), new Vector(1, -0.3, 0))
                .setKl(0.002).setKq(0.00002));

        scene.lights.add(new SpotLight(new Color(60, 55, 50), new Point(500, 200, -300), new Vector(-1, -0.3, 0))
                .setKl(0.002).setKq(0.00002));

        // === RENDU HAUTE QUALITÉ ===
        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .setMultithreading(-2)
                .build()
                .renderImage()
                .writeToImage("DiamondImage-first");
    }


    // grise
    @Test
    void diamondImageLuxuryShowcase() {
        // === MATÉRIAUX DIAMANT AMÉLIORÉS ===
        Material diamondMaterial = new Material()
                .setKD(0.01).setKS(0.99).setShininess(1000)  // KD réduit, KS augmenté
                .setKt(new Double3(0.88, 0.92, 0.96))
                .setKr(new Double3(0.7, 0.75, 0.8));         // Réflectivité légèrement réduite

        Material diamondMaterialBright = new Material()
                .setKD(0.005).setKS(0.995).setShininess(1200) // Encore plus réfléchissant
                .setKt(new Double3(0.90, 0.94, 0.98))
                .setKr(new Double3(0.75, 0.8, 0.85));

        Material diamondMaterialDark = new Material()
                .setKD(0.02).setKS(0.98).setShininess(800)
                .setKt(new Double3(0.85, 0.89, 0.94))
                .setKr(new Double3(0.65, 0.70, 0.75));        // Plus sombre pour contraste

        // Points de base du diamant
        Point center = new Point(0, 0, -290);
        Point topCenter = new Point(0, -150 + 50 + 170, -290);
        Point bottomCenter = new Point(0, -170, -340);

        List<Triangle> triangles = new ArrayList<>();

        // === CRÉATION DU DIAMANT (structure identique mais matériaux améliorés) ===
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

        // === PIÉDESTAL À 2 ÉTAGES EN VELOURS ROUGE ===
        Material pedestalMaterial = new Material()
                .setKD(0.8).setKS(0.2).setShininess(30)
                .setKr(new Double3(0.05, 0.05, 0.05));

        Material pedestalTopMaterial = new Material()
                .setKD(0.7).setKS(0.3).setShininess(50)
                .setKr(new Double3(0.1, 0.1, 0.1));

        // === ÉTAGE INFÉRIEUR (BASE) ===
        double pedestalBase1Height = -200;
        double pedestalBase1Radius = 250;
        double pedestalBase1Bottom = -280;
        int pedestalSides = 8;
        double pedestalAngleStep = 2 * Math.PI / pedestalSides;

        // Dessus de l'étage inférieur
        Point pedestalBase1Center = new Point(0, pedestalBase1Height, -290);
        for (int i = 0; i < pedestalSides; i++) {
            double angle1 = i * pedestalAngleStep;
            double angle2 = (i + 1) * pedestalAngleStep;

            Point p1 = new Point(
                    pedestalBase1Radius * Math.cos(angle1),
                    pedestalBase1Height,
                    -290 + pedestalBase1Radius * Math.sin(angle1)
            );
            Point p2 = new Point(
                    pedestalBase1Radius * Math.cos(angle2),
                    pedestalBase1Height,
                    -290 + pedestalBase1Radius * Math.sin(angle2)
            );

            Triangle pedestalTop = new Triangle(pedestalBase1Center, p1, p2);
            pedestalTop.setMaterial(pedestalTopMaterial)
                    .setEmission(new Color(70, 18, 18));
            triangles.add(pedestalTop);
        }

        // Côtés de l'étage inférieur
        for (int i = 0; i < pedestalSides; i++) {
            double angle1 = i * pedestalAngleStep;
            double angle2 = (i + 1) * pedestalAngleStep;

            Point p1Top = new Point(
                    pedestalBase1Radius * Math.cos(angle1),
                    pedestalBase1Height,
                    -290 + pedestalBase1Radius * Math.sin(angle1)
            );
            Point p2Top = new Point(
                    pedestalBase1Radius * Math.cos(angle2),
                    pedestalBase1Height,
                    -290 + pedestalBase1Radius * Math.sin(angle2)
            );
            Point p1Bottom = new Point(
                    pedestalBase1Radius * Math.cos(angle1),
                    pedestalBase1Bottom,
                    -290 + pedestalBase1Radius * Math.sin(angle1)
            );
            Point p2Bottom = new Point(
                    pedestalBase1Radius * Math.cos(angle2),
                    pedestalBase1Bottom,
                    -290 + pedestalBase1Radius * Math.sin(angle2)
            );

            Triangle side1 = new Triangle(p1Top, p1Bottom, p2Top);
            Triangle side2 = new Triangle(p2Top, p1Bottom, p2Bottom);

            side1.setMaterial(pedestalMaterial)
                    .setEmission(new Color(50, 12, 12));
            side2.setMaterial(pedestalMaterial)
                    .setEmission(new Color(50, 12, 12));
            triangles.add(side1);
            triangles.add(side2);
        }

        // === ÉTAGE SUPÉRIEUR (PLUS PETIT) ===
        double pedestalBase2Height = -180;
        double pedestalBase2Radius = 180;

        // Dessus de l'étage supérieur
        Point pedestalBase2Center = new Point(0, pedestalBase2Height, -290);
        for (int i = 0; i < pedestalSides; i++) {
            double angle1 = i * pedestalAngleStep;
            double angle2 = (i + 1) * pedestalAngleStep;

            Point p1 = new Point(
                    pedestalBase2Radius * Math.cos(angle1),
                    pedestalBase2Height,
                    -290 + pedestalBase2Radius * Math.sin(angle1)
            );
            Point p2 = new Point(
                    pedestalBase2Radius * Math.cos(angle2),
                    pedestalBase2Height,
                    -290 + pedestalBase2Radius * Math.sin(angle2)
            );

            Triangle pedestalTop = new Triangle(pedestalBase2Center, p1, p2);
            pedestalTop.setMaterial(pedestalTopMaterial)
                    .setEmission(new Color(85, 22, 22)); // Plus lumineux
            triangles.add(pedestalTop);
        }

        // Côtés de l'étage supérieur
        for (int i = 0; i < pedestalSides; i++) {
            double angle1 = i * pedestalAngleStep;
            double angle2 = (i + 1) * pedestalAngleStep;

            Point p1Top = new Point(
                    pedestalBase2Radius * Math.cos(angle1),
                    pedestalBase2Height,
                    -290 + pedestalBase2Radius * Math.sin(angle1)
            );
            Point p2Top = new Point(
                    pedestalBase2Radius * Math.cos(angle2),
                    pedestalBase2Height,
                    -290 + pedestalBase2Radius * Math.sin(angle2)
            );
            Point p1Bottom = new Point(
                    pedestalBase2Radius * Math.cos(angle1),
                    pedestalBase1Height,
                    -290 + pedestalBase2Radius * Math.sin(angle1)
            );
            Point p2Bottom = new Point(
                    pedestalBase2Radius * Math.cos(angle2),
                    pedestalBase1Height,
                    -290 + pedestalBase2Radius * Math.sin(angle2)
            );

            Triangle side1 = new Triangle(p1Top, p1Bottom, p2Top);
            Triangle side2 = new Triangle(p2Top, p1Bottom, p2Bottom);

            side1.setMaterial(pedestalMaterial)
                    .setEmission(new Color(65, 16, 16));
            side2.setMaterial(pedestalMaterial)
                    .setEmission(new Color(65, 16, 16));
            triangles.add(side1);
            triangles.add(side2);
        }

        // === MURS DE LA SALLE (BEIGE LUXUEUX) ===
        Material wallMaterial = new Material()
                .setKD(0.7).setKS(0.3).setShininess(40)
                .setKr(new Double3(0.1, 0.1, 0.1));

        // Mur arrière
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

        backgroundPlane1.setMaterial(wallMaterial)
                .setEmission(new Color(45, 40, 35)); // Beige chaud
        backgroundPlane2.setMaterial(wallMaterial)
                .setEmission(new Color(45, 40, 35));

        // Murs latéraux
        Triangle leftWall1 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(-1000, 600, -700),
                new Point(-1000, 600, 300)    // Étendu vers l'avant
        );
        Triangle leftWall2 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(-1000, 600, 300),   // Étendu vers l'avant
                new Point(-1000, -400, 300)
        );

        Triangle rightWall1 = new Triangle(
                new Point(1000, -400, -700),
                new Point(1000, 600, 300),    // Étendu vers l'avant
                new Point(1000, 600, -700)
        );
        Triangle rightWall2 = new Triangle(
                new Point(1000, -400, -700),
                new Point(1000, -400, 300),   // Étendu vers l'avant
                new Point(1000, 600, 300)
        );

        leftWall1.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));
        leftWall2.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));
        rightWall1.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));
        rightWall2.setMaterial(wallMaterial).setEmission(new Color(40, 35, 30));

        // === SOL EN MARBRE CLAIR ===
        Material marbleFloor = new Material()
                .setKD(0.4).setKS(0.6).setShininess(200)
                .setKr(new Double3(0.3, 0.3, 0.3));

        Triangle floor1 = new Triangle(
                new Point(-1000, -280, 300),   // Étendu vers l'avant
                new Point(1000, -280, 300),
                new Point(1000, -280, -700)
        );
        Triangle floor2 = new Triangle(
                new Point(-1000, -280, 300),   // Étendu vers l'avant
                new Point(1000, -280, -700),
                new Point(-1000, -280, -700)
        );

        floor1.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45)); // Marbre beige
        floor2.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45));

        // === PLAFOND ===
        Material ceilingMaterial = new Material()
                .setKD(0.8).setKS(0.2).setShininess(20)
                .setKr(new Double3(0.05, 0.05, 0.05));

        Triangle ceiling1 = new Triangle(
                new Point(-1000, 600, -700),
                new Point(1000, 600, 300),    // Étendu vers l'avant
                new Point(1000, 600, -700)
        );
        Triangle ceiling2 = new Triangle(
                new Point(-1000, 600, -700),
                new Point(-1000, 600, 300),   // Étendu vers l'avant
                new Point(1000, 600, 300)
        );

        ceiling1.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));
        ceiling2.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));

        // Cercle décoratif doré autour du piédestal (adapté au nouvel étage)
        Material goldAccent = new Material()
                .setKD(0.2).setKS(0.8).setShininess(300)
                .setKr(new Double3(0.4, 0.4, 0.2));

        int circleSegments = 16;
        double innerRadius = 280;  // Adapté au nouvel étage inférieur
        double outerRadius = 310;
        double circleAngleStep = 2 * Math.PI / circleSegments;

        for (int i = 0; i < circleSegments; i++) {
            double angle1 = i * circleAngleStep;
            double angle2 = (i + 1) * circleAngleStep;

            Point p1Inner = new Point(
                    innerRadius * Math.cos(angle1),
                    -279,
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

            ring1.setMaterial(goldAccent).setEmission(new Color(70, 18, 18));
            ring2.setMaterial(goldAccent).setEmission(new Color(70, 18, 18));
            triangles.add(ring1);
            triangles.add(ring2);
        }

        // Ajout de tous les triangles à la scène
        Triangle[] triangleArray = triangles.toArray(new Triangle[0]);
        scene.geometries.add(triangleArray);
        scene.geometries.add(backgroundPlane1, backgroundPlane2, floor1, floor2);
        scene.geometries.add(leftWall1, leftWall2, rightWall1, rightWall2);
        scene.geometries.add(ceiling1, ceiling2);


        scene.setAmbientLight(new AmbientLight(new Color(3, 5, 8)));  // Ambiance très douce

//       scene.lights.add(new SpotLight(new Color(151, 158, 172), new Point(0, 300, 200), new Vector(0, -1, -1))
//                .setKl(0.0001).setKq(0.000001));
////
        scene.lights.add(new SpotLight(new Color(120, 130, 150), new Point(-200, 200, 100), new Vector(1, -0.5, -0.8))
                .setKl(0.0008).setKq(0.000008));
//
        scene.lights.add(new SpotLight(new Color(150, 140, 120), new Point(200, 200, 100), new Vector(-1, -0.5, -0.8))
                .setKl(0.0008).setKq(0.000008));
//
        scene.lights.add(new DirectionalLight(new Color(10, 15, 25), new Vector(0, -0.3, -1)));

        // === RENDU HAUTE QUALITÉ ===
        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .setMultithreading(-2)
                .build()
                .renderImage()
                .writeToImage("DiamondImage-LuxuryShowcase");
    }

 //beige
 @Test
 void diamondbeige() {
     // === MATÉRIAUX DIAMANT MOINS TRANSPARENTS ===
     Material diamondMaterial = new Material()
             .setKD(0.02).setKS(0.98).setShininess(1000)
             .setKt(new Double3(0.65, 0.70, 0.75))  // Moins transparent
             .setKr(new Double3(0.8, 0.85, 0.9));   // Plus réfléchissant

     Material diamondMaterialBright = new Material()
             .setKD(0.015).setKS(0.985).setShininess(1200)
             .setKt(new Double3(0.70, 0.75, 0.80))  // Moins transparent
             .setKr(new Double3(0.85, 0.9, 0.95));

     Material diamondMaterialDark = new Material()
             .setKD(0.025).setKS(0.975).setShininess(800)
             .setKt(new Double3(0.60, 0.65, 0.70))  // Moins transparent
             .setKr(new Double3(0.75, 0.80, 0.85));

     // Points de base du diamant
     Point center = new Point(0, 0, -290);
     Point topCenter = new Point(0, -150 + 50 + 170, -290);
     Point bottomCenter = new Point(0, -170, -340);

     List<Triangle> triangles = new ArrayList<>();

     // === CRÉATION DU DIAMANT (structure identique mais matériaux améliorés) ===
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

     // === PIÉDESTAL À 2 ÉTAGES EN VELOURS ROUGE ===
     Material pedestalMaterial = new Material()
             .setKD(0.8).setKS(0.2).setShininess(30)
             .setKr(new Double3(0.05, 0.05, 0.05));

     Material pedestalTopMaterial = new Material()
             .setKD(0.7).setKS(0.3).setShininess(50)
             .setKr(new Double3(0.1, 0.1, 0.1));

     // === ÉTAGE INFÉRIEUR (BASE) ===
     double pedestalBase1Height = -200;
     double pedestalBase1Radius = 250;
     double pedestalBase1Bottom = -280;
     int pedestalSides = 8;
     double pedestalAngleStep = 2 * Math.PI / pedestalSides;

     // Dessus de l'étage inférieur
     Point pedestalBase1Center = new Point(0, pedestalBase1Height, -290);
     for (int i = 0; i < pedestalSides; i++) {
         double angle1 = i * pedestalAngleStep;
         double angle2 = (i + 1) * pedestalAngleStep;

         Point p1 = new Point(
                 pedestalBase1Radius * Math.cos(angle1),
                 pedestalBase1Height,
                 -290 + pedestalBase1Radius * Math.sin(angle1)
         );
         Point p2 = new Point(
                 pedestalBase1Radius * Math.cos(angle2),
                 pedestalBase1Height,
                 -290 + pedestalBase1Radius * Math.sin(angle2)
         );

         Triangle pedestalTop = new Triangle(pedestalBase1Center, p1, p2);
         pedestalTop.setMaterial(pedestalTopMaterial)
                 .setEmission(new Color(70, 18, 18));
         triangles.add(pedestalTop);
     }

     // Côtés de l'étage inférieur
     for (int i = 0; i < pedestalSides; i++) {
         double angle1 = i * pedestalAngleStep;
         double angle2 = (i + 1) * pedestalAngleStep;

         Point p1Top = new Point(
                 pedestalBase1Radius * Math.cos(angle1),
                 pedestalBase1Height,
                 -290 + pedestalBase1Radius * Math.sin(angle1)
         );
         Point p2Top = new Point(
                 pedestalBase1Radius * Math.cos(angle2),
                 pedestalBase1Height,
                 -290 + pedestalBase1Radius * Math.sin(angle2)
         );
         Point p1Bottom = new Point(
                 pedestalBase1Radius * Math.cos(angle1),
                 pedestalBase1Bottom,
                 -290 + pedestalBase1Radius * Math.sin(angle1)
         );
         Point p2Bottom = new Point(
                 pedestalBase1Radius * Math.cos(angle2),
                 pedestalBase1Bottom,
                 -290 + pedestalBase1Radius * Math.sin(angle2)
         );

         Triangle side1 = new Triangle(p1Top, p1Bottom, p2Top);
         Triangle side2 = new Triangle(p2Top, p1Bottom, p2Bottom);

         side1.setMaterial(pedestalMaterial)
                 .setEmission(new Color(50, 12, 12));
         side2.setMaterial(pedestalMaterial)
                 .setEmission(new Color(50, 12, 12));
         triangles.add(side1);
         triangles.add(side2);
     }

     // === ÉTAGE SUPÉRIEUR (PLUS PETIT) ===
     double pedestalBase2Height = -180;
     double pedestalBase2Radius = 180;

     // Dessus de l'étage supérieur
     Point pedestalBase2Center = new Point(0, pedestalBase2Height, -290);
     for (int i = 0; i < pedestalSides; i++) {
         double angle1 = i * pedestalAngleStep;
         double angle2 = (i + 1) * pedestalAngleStep;

         Point p1 = new Point(
                 pedestalBase2Radius * Math.cos(angle1),
                 pedestalBase2Height,
                 -290 + pedestalBase2Radius * Math.sin(angle1)
         );
         Point p2 = new Point(
                 pedestalBase2Radius * Math.cos(angle2),
                 pedestalBase2Height,
                 -290 + pedestalBase2Radius * Math.sin(angle2)
         );

         Triangle pedestalTop = new Triangle(pedestalBase2Center, p1, p2);
         pedestalTop.setMaterial(pedestalTopMaterial)
                 .setEmission(new Color(85, 22, 22)); // Plus lumineux
         triangles.add(pedestalTop);
     }

     // Côtés de l'étage supérieur
     for (int i = 0; i < pedestalSides; i++) {
         double angle1 = i * pedestalAngleStep;
         double angle2 = (i + 1) * pedestalAngleStep;

         Point p1Top = new Point(
                 pedestalBase2Radius * Math.cos(angle1),
                 pedestalBase2Height,
                 -290 + pedestalBase2Radius * Math.sin(angle1)
         );
         Point p2Top = new Point(
                 pedestalBase2Radius * Math.cos(angle2),
                 pedestalBase2Height,
                 -290 + pedestalBase2Radius * Math.sin(angle2)
         );
         Point p1Bottom = new Point(
                 pedestalBase2Radius * Math.cos(angle1),
                 pedestalBase1Height,
                 -290 + pedestalBase2Radius * Math.sin(angle1)
         );
         Point p2Bottom = new Point(
                 pedestalBase2Radius * Math.cos(angle2),
                 pedestalBase1Height,
                 -290 + pedestalBase2Radius * Math.sin(angle2)
         );

         Triangle side1 = new Triangle(p1Top, p1Bottom, p2Top);
         Triangle side2 = new Triangle(p2Top, p1Bottom, p2Bottom);

         side1.setMaterial(pedestalMaterial)
                 .setEmission(new Color(65, 16, 16));
         side2.setMaterial(pedestalMaterial)
                 .setEmission(new Color(65, 16, 16));
         triangles.add(side1);
         triangles.add(side2);
     }

     // === MATÉRIAUX MURS BEIGES AMÉLIORÉS ===
     Material wallMaterial = new Material()
             .setKD(0.8).setKS(0.2).setShininess(30)
             .setKr(new Double3(0.05, 0.05, 0.05));

     // Mur arrière
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

     backgroundPlane1.setMaterial(wallMaterial)
             .setEmission(new Color(120, 100, 80)); // Beige plus lumineux

     backgroundPlane2.setMaterial(wallMaterial)
             .setEmission(new Color(120, 100, 80));

     // Murs latéraux
     Triangle leftWall1 = new Triangle(
             new Point(-1000, -400, -700),
             new Point(-1000, 600, -700),
             new Point(-1000, 600, 300)    // Étendu vers l'avant
     );
     Triangle leftWall2 = new Triangle(
             new Point(-1000, -400, -700),
             new Point(-1000, 600, 300),   // Étendu vers l'avant
             new Point(-1000, -400, 300)
     );

     Triangle rightWall1 = new Triangle(
             new Point(1000, -400, -700),
             new Point(1000, 600, 300),    // Étendu vers l'avant
             new Point(1000, 600, -700)
     );
     Triangle rightWall2 = new Triangle(
             new Point(1000, -400, -700),
             new Point(1000, -400, 300),   // Étendu vers l'avant
             new Point(1000, 600, 300)
     );

     leftWall1.setMaterial(wallMaterial).setEmission(new Color(110, 90, 70));
     leftWall2.setMaterial(wallMaterial).setEmission(new Color(110, 90, 70));
     rightWall1.setMaterial(wallMaterial).setEmission(new Color(110, 90, 70));
     rightWall2.setMaterial(wallMaterial).setEmission(new Color(110, 90, 70));

     // === SOL EN MARBRE CLAIR ===
     Material marbleFloor = new Material()
             .setKD(0.4).setKS(0.6).setShininess(200)
             .setKr(new Double3(0.3, 0.3, 0.3));

     Triangle floor1 = new Triangle(
             new Point(-1000, -280, 300),   // Étendu vers l'avant
             new Point(1000, -280, 300),
             new Point(1000, -280, -700)
     );
     Triangle floor2 = new Triangle(
             new Point(-1000, -280, 300),   // Étendu vers l'avant
             new Point(1000, -280, -700),
             new Point(-1000, -280, -700)
     );

     floor1.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45)); // Marbre beige
     floor2.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45));

     // === PLAFOND ===
     Material ceilingMaterial = new Material()
             .setKD(0.8).setKS(0.2).setShininess(20)
             .setKr(new Double3(0.05, 0.05, 0.05));

     Triangle ceiling1 = new Triangle(
             new Point(-1000, 600, -700),
             new Point(1000, 600, 300),    // Étendu vers l'avant
             new Point(1000, 600, -700)
     );
     Triangle ceiling2 = new Triangle(
             new Point(-1000, 600, -700),
             new Point(-1000, 600, 300),   // Étendu vers l'avant
             new Point(1000, 600, 300)
     );

     ceiling1.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));
     ceiling2.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));

     // Cercle décoratif doré autour du piédestal (adapté au nouvel étage)
     Material goldAccent = new Material()
             .setKD(0.2).setKS(0.8).setShininess(300)
             .setKr(new Double3(0.4, 0.4, 0.2));

     int circleSegments = 16;
     double innerRadius = 280;  // Adapté au nouvel étage inférieur
     double outerRadius = 310;
     double circleAngleStep = 2 * Math.PI / circleSegments;

     for (int i = 0; i < circleSegments; i++) {
         double angle1 = i * circleAngleStep;
         double angle2 = (i + 1) * circleAngleStep;

         Point p1Inner = new Point(
                 innerRadius * Math.cos(angle1),
                 -279,
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

         ring1.setMaterial(goldAccent).setEmission(new Color(70, 18, 18));
         ring2.setMaterial(goldAccent).setEmission(new Color(70, 18, 18));
         triangles.add(ring1);
         triangles.add(ring2);
     }

     // Ajout de tous les triangles à la scène
     Triangle[] triangleArray = triangles.toArray(new Triangle[0]);
     scene.geometries.add(triangleArray);
     scene.geometries.add(backgroundPlane1, backgroundPlane2, floor1, floor2);
     scene.geometries.add(leftWall1, leftWall2, rightWall1, rightWall2);
     scene.geometries.add(ceiling1, ceiling2);


     // === ÉCLAIRAGE OPTIMISÉ ===
// Lumière ambiante équilibrée pour révéler les couleurs
     scene.setAmbientLight(new AmbientLight(new Color(15, 12, 10))); // Ambiance beige chaud


// Lumière douce focalisée sur le diamant pour ses reflets
     scene.lights.add(new SpotLight(new Color(40, 45, 50), new Point(0, 200, 150), new Vector(0, -0.8, -0.6))
             .setKl(0.001).setKq(0.00001));

// Lumières latérales plus intenses pour les murs beiges
     scene.lights.add(new SpotLight(new Color(160, 130, 100), new Point(-300, 200, 100), new Vector(1, -0.3, -0.6))
             .setKl(0.0005).setKq(0.000005));

     scene.lights.add(new SpotLight(new Color(160, 130, 100), new Point(300, 200, 100), new Vector(-1, -0.3, -0.6))
             .setKl(0.0005).setKq(0.000005));
     // Spot décoratif sur le mur arrière au-dessus du diamant
     scene.lights.add(new SpotLight(new Color(80, 65, 50), new Point(0, 100, -200), new Vector(0, 0, -1))
             .setKl(0.002).setKq(0.00002));

// Lumière d'arrière-plan pour les murs
     scene.lights.add(new DirectionalLight(new Color(40, 35, 25), new Vector(0, 0, -1)));

// Lumière douce du haut pour unifier la scène
     scene.lights.add(new DirectionalLight(new Color(30, 25, 20), new Vector(0, -1, 0)));

     // === RENDU HAUTE QUALITÉ ===
     cameraBuilder
             .setLocation(new Point(0, 50, 200))
             .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
             .setVpDistance(300).setVpSize(600, 600)
             .setResolution(800, 800)
             .setMultithreading(-2)
             .build()
             .renderImage()
             .writeToImage("DiamondImage-beige");
 }
}
