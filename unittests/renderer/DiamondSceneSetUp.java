package renderer;

import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class DiamondSceneSetUp {
    /**
     * Sets up the diamond scene with a pedestal and enhanced materials.
     * The diamond is created with a more complex structure and improved materials.
     */
    public static void setUpDiamond(Scene scene){
        // === Diamond material ===
        Material diamondMaterial = new Material()
                .setKD(0.1).setKS(0.9).setShininess(300)
                .setKt(new Double3(0.75, 0.95, 0.98))
                .setKr(new Double3(0.4, 0.5, 0.6));

        Material diamondMaterialBright = new Material()
                .setKD(0.08).setKS(0.92).setShininess(400)
                .setKt(new Double3(0.78, 0.96, 0.99))
                .setKr(new Double3(0.45, 0.55, 0.65));

        Material diamondMaterialDark = new Material()
                .setKD(0.12).setKS(0.88).setShininess(250)
                .setKt(new Double3(0.70, 0.82, 0.96))
                .setKr(new Double3(0.55, 0.55, 0.65));


        Point center = new Point(0, -50, -290);
        Point topCenter = new Point(0, -150 + 50 + 170 - 50, -290);
        Point bottomCenter = new Point(0, -140, -310);

        List<Triangle> triangles = new ArrayList<>();

        // === CREATION OF THE DIAMOND ===
        double angleStep = 2 * Math.PI / 24;
        double radiusMiddle = 100;
        double heightMiddle = 60;

        for (int i = 0; i < 24; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            Point p1Mid = new Point(
                    radiusMiddle * Math.cos(angle1),
                    heightMiddle - 50,  // Adjusted to follow the center
                    -270 + radiusMiddle * Math.sin(angle1) * 0.2
            );
            Point p2Mid = new Point(
                    radiusMiddle * Math.cos(angle2),
                    heightMiddle - 50,  // Adjusted to follow the center
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

        // Diamond Pavilion
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

        //=== 2-TIER RED VELVET PEDESTAL ===
        Material pedestalMaterial = new Material()
                .setKD(0.8).setKS(0.2).setShininess(30)
                .setKr(new Double3(0.05, 0.05, 0.05));

        Material pedestalTopMaterial = new Material()
                .setKD(0.7).setKS(0.3).setShininess(50)
                .setKr(new Double3(0.1, 0.1, 0.1));

        //=== LOWER FLOOR (BASE) ===
        double pedestalBase1Height = -200;
        double pedestalBase1Radius = 250;
        double pedestalBase1Bottom = -280;
        int pedestalSides = 8;
        double pedestalAngleStep = 2 * Math.PI / pedestalSides;

        //Above the lower floor
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
                    .setEmission(new Color(170, 12, 12));
            triangles.add(pedestalTop);
        }

        // Sides of the lower floor
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

        // === UPPER FLOOR (SMALLER) ===
        double pedestalBase2Height = -180;
        double pedestalBase2Radius = 180;

        // Above the upper floor
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
                    .setEmission(new Color(85, 22, 22));
            triangles.add(pedestalTop);
        }

        // Sides of the upper floor
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


        // === ROOM WALLS (BEIGE) ===
        Material wallMaterial = new Material()
                .setKD(0.7).setKS(0.3).setShininess(40)
                .setKr(new Double3(0.1, 0.1, 0.1));

        // Back wall
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
                .setEmission(new Color(86, 74, 63));
        backgroundPlane2.setMaterial(wallMaterial)
                .setEmission(new Color(86, 74, 63));

        //Side walls
        Triangle leftWall1 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(-1000, 600, -700),
                new Point(-1000, 600, 300)    // Extended forward
        );
        Triangle leftWall2 = new Triangle(
                new Point(-1000, -400, -700),
                new Point(-1000, 600, 300),   // Extended forward
                new Point(-1000, -400, 300)
        );

        Triangle rightWall1 = new Triangle(
                new Point(1000, -400, -700),
                new Point(1000, 600, 300),    // Extended forward
                new Point(1000, 600, -700)
        );
        Triangle rightWall2 = new Triangle(
                new Point(1000, -400, -700),
                new Point(1000, -400, 300),   // Extended forward
                new Point(1000, 600, 300)
        );

        leftWall1.setMaterial(wallMaterial).setEmission(new Color(86, 74, 63));
        leftWall2.setMaterial(wallMaterial).setEmission(new Color(86, 74, 63));
        rightWall1.setMaterial(wallMaterial).setEmission(new Color(86, 74, 63));
        rightWall2.setMaterial(wallMaterial).setEmission(new Color(86, 74, 63));

        // === LIGHT MARBLE FLOOR ===
        Material marbleFloor = new Material()
                .setKD(0.4).setKS(0.6).setShininess(200)
                .setKr(new Double3(0.3, 0.3, 0.3));

        Triangle floor1 = new Triangle(
                new Point(-1000, -280, 300),   // Extended forward
                new Point(1000, -280, 300),
                new Point(1000, -280, -700)
        );
        Triangle floor2 = new Triangle(
                new Point(-1000, -280, 300),   // Extended forward
                new Point(1000, -280, -700),
                new Point(-1000, -280, -700)
        );

        floor1.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45)); //Beige marble
        floor2.setMaterial(marbleFloor).setEmission(new Color(55, 50, 45));

        // === CEILING ===
        Material ceilingMaterial = new Material()
                .setKD(0.8).setKS(0.2).setShininess(20)
                .setKr(new Double3(0.05, 0.05, 0.05));

        Triangle ceiling1 = new Triangle(
                new Point(-1000, 600, -700),
                new Point(1000, 600, 300),    // Extended forward
                new Point(1000, 600, -700)
        );
        Triangle ceiling2 = new Triangle(
                new Point(-1000, 600, -700),
                new Point(-1000, 600, 300),   // Extended forward
                new Point(1000, 600, 300)
        );

        ceiling1.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));
        ceiling2.setMaterial(ceilingMaterial).setEmission(new Color(50, 45, 40));

        // Decorative gold circle around the pedestal
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

            ring1.setMaterial(goldAccent).setEmission(new Color(80, 60, 20)); // Gold
            ring2.setMaterial(goldAccent).setEmission(new Color(80, 60, 20));
            triangles.add(ring1);
            triangles.add(ring2);
        }

        // Adding all triangles to the scene
        Triangle[] triangleArray = triangles.toArray(new Triangle[0]);
        scene.geometries.add(triangleArray);
        scene.geometries.add(backgroundPlane1, backgroundPlane2, floor1, floor2);
        scene.geometries.add(leftWall1, leftWall2, rightWall1, rightWall2);
        scene.geometries.add(ceiling1, ceiling2);
        // === DIAMOND-OPTIMIZED LIGHTING ===
        scene.setAmbientLight(new AmbientLight(new Color(8, 6, 4)));

        // Main light - focused on the diamond
        scene.lights.add(new SpotLight(new Color(150, 140, 120), new Point(0, 400, -150), new Vector(0, -1, -0.2))
                .setKl(0.0001).setKq(0.000001));

        //Side lighting to create prismatic reflections
        scene.lights.add(new SpotLight(new Color(80, 90, 120), new Point(-300, 200, -100), new Vector(1, -0.8, -0.5))
                .setKl(0.0005).setKq(0.000005));

        scene.lights.add(new SpotLight(new Color(120, 80, 90), new Point(300, 200, -100), new Vector(-1, -0.8, -0.5))
                .setKl(0.0005).setKq(0.000005));

        //Accent light from the back for transparency
        scene.lights.add(new SpotLight(new Color(60, 70, 90), new Point(0, 100, -500), new Vector(0, 0, 1))
                .setKl(0.001).setKq(0.00001));

        // Very soft directional lighting for general ambiance
        scene.lights.add(new DirectionalLight(new Color(5, 4, 3), new Vector(0.3, -0.7, -1)));

        // Lights for walls (reduced)
        scene.lights.add(new SpotLight(new Color(25, 20, 15), new Point(-600, 200, -300), new Vector(1, -0.2, 0))
                .setKl(0.005).setKq(0.00005));

        scene.lights.add(new SpotLight(new Color(25, 20, 15), new Point(600, 200, -300), new Vector(-1, -0.2, 0))
                .setKl(0.005).setKq(0.00005));


        // Specific light for the summit
        scene.lights.add(new SpotLight(new Color(100, 110, 130), new Point(0, 300, -200), new Vector(0, -1, -0.3))
                .setKl(0.0003).setKq(0.000003));
    }
}
