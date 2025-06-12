package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import org.junit.jupiter.api.Test;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class Minip1Tests {
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);


    @Test
    void diamondImage2() {
        // Diamant amélioré avec plus de triangles par facette

// Matériau principal du diamant
        Material diamondMaterial = new Material()
                .setKD(0.05).setKS(0.95).setShininess(400)
                .setKt(new Double3(0.8, 0.88, 0.95))
                .setKr(new Double3(0.4, 0.5, 0.6));

        Material diamondMaterialDark = new Material()
                .setKD(0.05).setKS(0.95).setShininess(400)
                .setKt(new Double3(0.75, 0.85, 0.92))
                .setKr(new Double3(0.5, 0.6, 0.7));

// Points de base du diamant
        Point center = new Point(0, 0, -290);
        Point topCenter = new Point(0, 80, -290);
        Point bottomCenter = new Point(0, -120, -300);

// Génération du diamant sans couronne
        List<Triangle> triangles = new ArrayList<>();

// Configuration de base
        double angleStep = 2 * Math.PI / 16;
        double radiusMiddle = 50;
        double heightMiddle = 30;

// Partie supérieure plate - surface circulaire avec 16 triangles
        for (int i = 0; i < 16; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            Point p1Mid = new Point(
                    radiusMiddle * Math.cos(angle1),
                    heightMiddle,
                    -280 + radiusMiddle * Math.sin(angle1) * 0.2
            );
            Point p2Mid = new Point(
                    radiusMiddle * Math.cos(angle2),
                    heightMiddle,
                    -280 + radiusMiddle * Math.sin(angle2) * 0.2
            );

            Triangle triangle =new Triangle(center, p1Mid, p2Mid);
            triangle
                    .setEmission(new Color(10 + i % 4, 15 + i % 3, 25 + i % 4))
                    .setMaterial(diamondMaterial);
            // Triangle formant la surface supérieure plate
            triangles.add(triangle);
        }
        double radiusLarge = 80;
        double heightLow = -10;

        for (int i = 0; i < 16; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            Point p1Mid = new Point(
                    radiusMiddle * Math.cos(angle1),
                    heightMiddle,
                    -280 + radiusMiddle * Math.sin(angle1) * 0.2
            );
            Point p2Mid = new Point(
                    radiusMiddle * Math.cos(angle2),
                    heightMiddle,
                    -280 + radiusMiddle * Math.sin(angle2) * 0.2
            );

            Point p1Large = new Point(
                    radiusLarge * Math.cos(angle1),
                    heightLow,
                    -270 + radiusLarge * Math.sin(angle1) * 0.15
            );
            Point p2Large = new Point(
                    radiusLarge * Math.cos(angle2),
                    heightLow,
                    -270 + radiusLarge * Math.sin(angle2) * 0.15
            );

            // Triangles de la ceinture
            Triangle triangle = new Triangle(p1Mid, p1Large, p2Mid);
            triangle
                    .setEmission(new Color(12 + i % 3, 18 + i % 4, 28 + i % 3))
                    .setMaterial(diamondMaterialDark);
            triangles.add(triangle);

            triangle=new Triangle(p2Mid, p1Large, p2Large);
            triangle
                    .setEmission(new Color(12 + i % 3, 18 + i % 4, 28 + i % 3))
                    .setMaterial(diamondMaterialDark);
            triangles.add(triangle);
        }

// Pavillon (partie inférieure) - 16 triangles
        for (int i = 0; i < 16; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            Point p1Large = new Point(
                    radiusLarge * Math.cos(angle1),
                    heightLow,
                    -270 + radiusLarge * Math.sin(angle1) * 0.15
            );
            Point p2Large = new Point(
                    radiusLarge * Math.cos(angle2),
                    heightLow,
                    -270 + radiusLarge * Math.sin(angle2) * 0.15
            );
            Triangle triangle = new Triangle(p1Large, bottomCenter, p2Large);
            triangle
                    .setEmission(new Color(15 + i % 4, 22 + i % 3, 32 + i % 5))
                    .setMaterial(new Material()
                            .setKD(0.05).setKS(0.95).setShininess(400)
                            .setKt(new Double3(0.7, 0.8, 0.88))
                            .setKr(new Double3(0.6, 0.7, 0.8)));
            // Triangle du pavillon
            triangles.add(triangle);
        }

// Facettes additionnelles pour la surface supérieure
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            double smallRadius = 25;

            Point p1 = new Point(
                    smallRadius * Math.cos(angle),
                    heightMiddle,
                    -280 + smallRadius * Math.sin(angle) * 0.2
            );
            Point p2 = new Point(
                    smallRadius * Math.cos(angle + Math.PI/4),
                    heightMiddle,
                    -280 + smallRadius * Math.sin(angle + Math.PI/4) * 0.2
            );
            Triangle triangle =new Triangle(center, p1, p2);
            triangle
                    .setEmission(new Color(8 + i % 2, 12 + i % 3, 20 + i % 4))
                    .setMaterial(diamondMaterial);
            triangles.add(triangle);
        }

// Ajout de tous les triangles à la scène
        Triangle[] triangleArray = triangles.toArray(new Triangle[0]);
        scene.geometries.add(triangleArray);


        // === ÉCLAIRAGE PROFESSIONNEL === (4+ sources)
        scene.setAmbientLight(new AmbientLight(new Color(3, 5, 12)));

        // Lumière principale - spot focalisé sur le diamant
        scene.lights.add(new SpotLight(new Color(180, 200, 240), new Point(0, 200, 150), new Vector(0, -1, -1))
                .setKl(0.0003).setKq(0.000003));

        // Lumière de remplissage douce
        scene.lights.add(new SpotLight(new Color(120, 140, 180), new Point(-150, 150, 100), new Vector(0.8, -0.6, -0.8))
                .setKl(0.0008).setKq(0.000008));

        // Lumière d'accentuation latérale
        scene.lights.add(new SpotLight(new Color(140, 120, 180), new Point(150, 150, 100), new Vector(-0.8, -0.6, -0.8))
                .setKl(0.0008).setKq(0.000008));

        // Lumière d'arrière-plan très douce pour l'atmosphère
        scene.lights.add(new DirectionalLight(new Color(20, 30, 50), new Vector(0, -0.3, -1)));

        // Lumière ponctuelle pour les reflets
        scene.lights.add(new PointLight(new Color(60, 80, 120), new Point(0, 100, -200))
                .setKl(0.001).setKq(0.00001));


        // === RENDU SANS EFFETS ===
        cameraBuilder
                .setLocation(new Point(0, 0, 300))
                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(400, 400)
                .setResolution(500, 500)
                // .setAntiAliasing(false) // Désactiver anti-aliasing
                // .setGlossyReflection(false) // Désactiver glossy
                .build()
                .renderImage()
                .writeToImage("DiamondImage_NoEffects");

        // === RENDU AVEC EFFETS ===
        cameraBuilder
                .setLocation(new Point(0, 0, 70))
                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(400, 400)
                .setResolution(500, 500)
                // .setAntiAliasing(true) // Activer anti-aliasing
                // .setAntiAliasingRays(81) // 9x9 grid (ou plus pour la qualité finale)
                // .setGlossyReflection(true) // Activer glossy reflection
                // .setGlossyRays(100) // Nombre de rayons pour l'effet glossy
                .build()
                .renderImage()
                .writeToImage("DiamondImage");
    }

    @Test
    void diamondImageWithAntiAliasingAndGlossy() {
        // Configuration des paramètres pour Anti-Aliasing et Glossy Surface
        // Ces setters devront être implémentés dans vos classes appropriées

        // Anti-Aliasing parameters (à implémenter dans Camera ou Renderer)
        // cameraBuilder.setAntiAliasing(true).setAntiAliasingRays(81); // 9x9 grid
        // ou
        // renderer.setAntiAliasing(true).setAntiAliasingRays(81);

        // Glossy Surface parameters (à implémenter dans Material)
        // Material.setGlossyReflection(true).setGlossyRays(100);

        // === GÉOMÉTRIES DU DIAMANT BRILLIANT CUT === (15+ géométries)

        // COURONNE (Crown) - Partie supérieure
        // Table centrale (octogone simplifié en triangles)
        scene.geometries.add(
                new Triangle(new Point(-30, 80, -290), new Point(30, 80, -290), new Point(30, 80, -310))
                        .setEmission(new Color(8, 12, 20))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.85, 0.92, 0.98))
                                .setKr(new Double3(0.3, 0.4, 0.5))),

                new Triangle(new Point(30, 80, -310), new Point(-30, 80, -310), new Point(-30, 80, -290))
                        .setEmission(new Color(8, 12, 20))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.85, 0.92, 0.98))
                                .setKr(new Double3(0.3, 0.4, 0.5))),

                // Facettes étoile (Star facets) - 8 facettes autour de la table
                new Triangle(new Point(-30, 80, -290), new Point(-60, 30, -280), new Point(-42, 55, -290))
                        .setEmission(new Color(10, 15, 25))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.8, 0.88, 0.95))
                                .setKr(new Double3(0.4, 0.5, 0.6))),

                new Triangle(new Point(30, 80, -290), new Point(42, 55, -290), new Point(60, 30, -280))
                        .setEmission(new Color(10, 15, 25))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.8, 0.88, 0.95))
                                .setKr(new Double3(0.4, 0.5, 0.6))),

                new Triangle(new Point(-30, 80, -310), new Point(-42, 55, -310), new Point(-60, 30, -320))
                        .setEmission(new Color(10, 15, 25))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.8, 0.88, 0.95))
                                .setKr(new Double3(0.4, 0.5, 0.6))),

                new Triangle(new Point(30, 80, -310), new Point(60, 30, -320), new Point(42, 55, -310))
                        .setEmission(new Color(10, 15, 25))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.8, 0.88, 0.95))
                                .setKr(new Double3(0.4, 0.5, 0.6))),

                // Facettes principales de la couronne (Crown main facets)
                new Triangle(new Point(-60, 30, -280), new Point(-80, 0, -270), new Point(-60, 30, -320))
                        .setEmission(new Color(12, 18, 28))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.75, 0.85, 0.92))
                                .setKr(new Double3(0.5, 0.6, 0.7))),

                new Triangle(new Point(60, 30, -280), new Point(60, 30, -320), new Point(80, 0, -270))
                        .setEmission(new Color(12, 18, 28))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.75, 0.85, 0.92))
                                .setKr(new Double3(0.5, 0.6, 0.7))),

                new Triangle(new Point(-60, 30, -320), new Point(-80, 0, -330), new Point(60, 30, -320))
                        .setEmission(new Color(12, 18, 28))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.75, 0.85, 0.92))
                                .setKr(new Double3(0.5, 0.6, 0.7))),

                new Triangle(new Point(60, 30, -320), new Point(80, 0, -330), new Point(80, 0, -270))
                        .setEmission(new Color(12, 18, 28))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.75, 0.85, 0.92))
                                .setKr(new Double3(0.5, 0.6, 0.7))),

                // Facettes supplémentaires pour enrichir la couronne

                    new Triangle(new Point(-30, 80, -290), new Point(-42, 55, -290), new Point(-15, 70, -300))
                            .setEmission(new Color(10, 15, 25))
                            .setMaterial(/* même matériau */ new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.8, 0.88, 0.95))
                                    .setKr(new Double3(0.4, 0.5, 0.6))),

                    new Triangle(new Point(30, 80, -290), new Point(15, 70, -300), new Point(42, 55, -290))
                            .setEmission(new Color(10, 15, 25))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.8, 0.88, 0.95))
                                    .setKr(new Double3(0.4, 0.5, 0.6))),

                    new Triangle(new Point(-15, 70, -300), new Point(0, 60, -290), new Point(15, 70, -300))
                            .setEmission(new Color(10, 15, 25))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.8, 0.88, 0.95))
                                    .setKr(new Double3(0.4, 0.5, 0.6))),

                    new Triangle(new Point(-42, 55, -290), new Point(-60, 30, -280), new Point(-70, 10, -300))
                            .setEmission(new Color(12, 18, 28))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.75, 0.85, 0.92))
                                    .setKr(new Double3(0.5, 0.6, 0.7))),

                    new Triangle(new Point(42, 55, -290), new Point(70, 10, -300), new Point(60, 30, -280))
                            .setEmission(new Color(12, 18, 28))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.75, 0.85, 0.92))
                                    .setKr(new Double3(0.5, 0.6, 0.7))),

                    new Triangle(new Point(-60, 30, -280), new Point(-80, 0, -270), new Point(-70, 10, -300))
                            .setEmission(new Color(12, 18, 28))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.75, 0.85, 0.92))
                                    .setKr(new Double3(0.5, 0.6, 0.7))),

                    new Triangle(new Point(60, 30, -280), new Point(70, 10, -300), new Point(80, 0, -270))
                            .setEmission(new Color(12, 18, 28))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.75, 0.85, 0.92))
                                    .setKr(new Double3(0.5, 0.6, 0.7))),

                    new Triangle(new Point(-70, 10, -300), new Point(0, -120, -300), new Point(-80, 0, -330))
                            .setEmission(new Color(15, 22, 32))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.7, 0.8, 0.88))
                                    .setKr(new Double3(0.6, 0.7, 0.8))),

                    new Triangle(new Point(70, 10, -300), new Point(80, 0, -330), new Point(0, -120, -300))
                            .setEmission(new Color(15, 22, 32))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.7, 0.8, 0.88))
                                    .setKr(new Double3(0.6, 0.7, 0.8))),

                    new Triangle(new Point(-60, 30, -320), new Point(-70, 10, -300), new Point(-80, 0, -330))
                            .setEmission(new Color(12, 18, 28))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.75, 0.85, 0.92))
                                    .setKr(new Double3(0.5, 0.6, 0.7))),

                    new Triangle(new Point(70, 10, -300), new Point(60, 30, -320), new Point(80, 0, -330))
                            .setEmission(new Color(12, 18, 28))
                            .setMaterial(new Material()
                                    .setKD(0.05).setKS(0.95).setShininess(400)
                                    .setKt(new Double3(0.75, 0.85, 0.92))
                                    .setKr(new Double3(0.5, 0.6, 0.7))),


                // PAVILLON (Pavilion) - Partie inférieure pointue
                // Facettes principales du pavillon
                new Triangle(new Point(-80, 0, -270), new Point(0, -120, -300), new Point(-80, 0, -330))
                        .setEmission(new Color(15, 22, 32))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.7, 0.8, 0.88))
                                .setKr(new Double3(0.6, 0.7, 0.8))),

                new Triangle(new Point(80, 0, -270), new Point(80, 0, -330), new Point(0, -120, -300))
                        .setEmission(new Color(15, 22, 32))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.7, 0.8, 0.88))
                                .setKr(new Double3(0.6, 0.7, 0.8))),

                new Triangle(new Point(-80, 0, -330), new Point(0, -120, -300), new Point(80, 0, -330))
                        .setEmission(new Color(15, 22, 32))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.7, 0.8, 0.88))
                                .setKr(new Double3(0.6, 0.7, 0.8))),

                new Triangle(new Point(80, 0, -270), new Point(0, -120, -300), new Point(-80, 0, -270))
                        .setEmission(new Color(15, 22, 32))
                        .setMaterial(new Material()
                                .setKD(0.05).setKS(0.95).setShininess(400)
                                .setKt(new Double3(0.7, 0.8, 0.88))
                                .setKr(new Double3(0.6, 0.7, 0.8))),

                // Surface de support réfléchissante (écrin)
                new Plane(new Point(0, -150, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(2, 5, 15))
                        .setMaterial(new Material()
                                .setKD(0.3).setKS(0.7).setShininess(200)
                                .setKr(new Double3(0.4, 0.5, 0.8))),

                // Fond sombre élégant
                new Plane(new Point(0, 0, -450), new Vector(0, 0, 1))
                        .setEmission(new Color(1, 2, 8))
                        .setMaterial(new Material()
                                .setKD(0.8).setKS(0.2).setShininess(30)),

                // Sphères décoratives pour l'environnement
                new Sphere(15d, new Point(-150, -100, -250))
                        .setEmission(new Color(5, 8, 15))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.6).setShininess(100)
                                .setKt(new Double3(0.6, 0.7, 0.8))),

                new Sphere(15d, new Point(150, -100, -250))
                        .setEmission(new Color(8, 5, 15))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.6).setShininess(100)
                                .setKt(new Double3(0.8, 0.6, 0.7))),

                new Sphere(12d, new Point(0, -120, -180))
                        .setEmission(new Color(5, 15, 8))
                        .setMaterial(new Material()
                                .setKD(0.4).setKS(0.6).setShininess(100)
                                .setKt(new Double3(0.7, 0.8, 0.6)))
        );

        // === ÉCLAIRAGE PROFESSIONNEL === (4+ sources)
        scene.setAmbientLight(new AmbientLight(new Color(3, 5, 12)));

        // Lumière principale - spot focalisé sur le diamant
        scene.lights.add(new SpotLight(new Color(180, 200, 240), new Point(0, 200, 150), new Vector(0, -1, -1))
                .setKl(0.0003).setKq(0.000003));

        // Lumière de remplissage douce
        scene.lights.add(new SpotLight(new Color(120, 140, 180), new Point(-150, 150, 100), new Vector(0.8, -0.6, -0.8))
                .setKl(0.0008).setKq(0.000008));

        // Lumière d'accentuation latérale
        scene.lights.add(new SpotLight(new Color(140, 120, 180), new Point(150, 150, 100), new Vector(-0.8, -0.6, -0.8))
                .setKl(0.0008).setKq(0.000008));

        // Lumière d'arrière-plan très douce pour l'atmosphère
        scene.lights.add(new DirectionalLight(new Color(20, 30, 50), new Vector(0, -0.3, -1)));

        // Lumière ponctuelle pour les reflets
        scene.lights.add(new PointLight(new Color(60, 80, 120), new Point(0, 100, -200))
                .setKl(0.001).setKq(0.00001));

        // === RENDU SANS EFFETS ===
        cameraBuilder
                .setLocation(new Point(0, 0, 300))
                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(400, 400)
                .setResolution(500, 500)
                // .setAntiAliasing(false) // Désactiver anti-aliasing
                // .setGlossyReflection(false) // Désactiver glossy
                .build()
                .renderImage()
                .writeToImage("DiamondImage_NoEffects");

        // === RENDU AVEC EFFETS ===
        cameraBuilder
                .setLocation(new Point(0, 0, 300))
                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(400, 400)
                .setResolution(500, 500)
                // .setAntiAliasing(true) // Activer anti-aliasing
                // .setAntiAliasingRays(81) // 9x9 grid (ou plus pour la qualité finale)
                // .setGlossyReflection(true) // Activer glossy reflection
                // .setGlossyRays(100) // Nombre de rayons pour l'effet glossy
                .build()
                .renderImage()
                .writeToImage("DiamondImage_WithEffects");
    }

//    void diamondImageWithAntiAliasingAndGlossy() {
//        // Configuration des paramètres pour Anti-Aliasing et Glossy Surface
//        // Ces setters devront être implémentés dans vos classes appropriées
//
//        // Anti-Aliasing parameters (à implémenter dans Camera ou Renderer)
//        // cameraBuilder.setAntiAliasing(true).setAntiAliasingRays(81); // 9x9 grid
//        // ou
//        // renderer.setAntiAliasing(true).setAntiAliasingRays(81);
//
//        // Glossy Surface parameters (à implémenter dans Material)
//        // Material.setGlossyReflection(true).setGlossyRays(100);
//
//        // === GÉOMÉTRIES DU DIAMANT === (12+ géométries)
//
//        // Facettes supérieures du diamant (crown) - Table principale
//        scene.geometries.add(
//                new Triangle(new Point(-60, 50, -300), new Point(60, 50, -300), new Point(60, 50, -350))
//                        .setEmission(new Color(5, 10, 15))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.8, 0.9, 0.95))
//                                .setKr(new Double3(0.2, 0.3, 0.4))),
//
//                new Triangle(new Point(60, 50, -350), new Point(-60, 50, -350), new Point(-60, 50, -300))
//                        .setEmission(new Color(5, 10, 15))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.8, 0.9, 0.95))
//                                .setKr(new Double3(0.2, 0.3, 0.4))),
//
//                // Facettes latérales supérieures
//                new Triangle(new Point(-60, 50, -300), new Point(-100, -50, -280), new Point(-60, 50, -350))
//                        .setEmission(new Color(8, 12, 18))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.7, 0.8, 0.9))
//                                .setKr(new Double3(0.3, 0.4, 0.5))),
//
//                new Triangle(new Point(-60, 50, -350), new Point(-100, -50, -280), new Point(-100, -50, -370))
//                        .setEmission(new Color(8, 12, 18))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.7, 0.8, 0.9))
//                                .setKr(new Double3(0.3, 0.4, 0.5))),
//
//                new Triangle(new Point(60, 50, -300), new Point(60, 50, -350), new Point(100, -50, -280))
//                        .setEmission(new Color(8, 12, 18))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.7, 0.8, 0.9))
//                                .setKr(new Double3(0.3, 0.4, 0.5))),
//
//                new Triangle(new Point(60, 50, -350), new Point(100, -50, -370), new Point(100, -50, -280))
//                        .setEmission(new Color(8, 12, 18))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.7, 0.8, 0.9))
//                                .setKr(new Double3(0.3, 0.4, 0.5))),
//
//                // Facettes avant et arrière
//                new Triangle(new Point(-60, 50, -300), new Point(60, 50, -300), new Point(0, -50, -250))
//                        .setEmission(new Color(10, 15, 20))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.6, 0.7, 0.85))
//                                .setKr(new Double3(0.4, 0.5, 0.6))),
//
//                new Triangle(new Point(-60, 50, -350), new Point(0, -50, -400), new Point(60, 50, -350))
//                        .setEmission(new Color(10, 15, 20))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.6, 0.7, 0.85))
//                                .setKr(new Double3(0.4, 0.5, 0.6))),
//
//                // Facettes inférieures du diamant (pavilion)
//                new Triangle(new Point(-100, -50, -280), new Point(0, -200, -325), new Point(0, -50, -250))
//                        .setEmission(new Color(12, 18, 25))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.5, 0.6, 0.8))
//                                .setKr(new Double3(0.5, 0.6, 0.7))),
//
//                new Triangle(new Point(100, -50, -280), new Point(0, -50, -250), new Point(0, -200, -325))
//                        .setEmission(new Color(12, 18, 25))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.5, 0.6, 0.8))
//                                .setKr(new Double3(0.5, 0.6, 0.7))),
//
//                new Triangle(new Point(-100, -50, -370), new Point(0, -200, -325), new Point(0, -50, -400))
//                        .setEmission(new Color(12, 18, 25))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.5, 0.6, 0.8))
//                                .setKr(new Double3(0.5, 0.6, 0.7))),
//
//                new Triangle(new Point(100, -50, -370), new Point(0, -50, -400), new Point(0, -200, -325))
//                        .setEmission(new Color(12, 18, 25))
//                        .setMaterial(new Material()
//                                .setKD(0.1).setKS(0.9).setShininess(300)
//                                .setKt(new Double3(0.5, 0.6, 0.8))
//                                .setKr(new Double3(0.5, 0.6, 0.7))),
//
//                // Surface de support (plan réfléchissant sombre)
//                new Plane(new Point(0, -250, 0), new Vector(0, 1, 0))
//                        .setEmission(new Color(5, 5, 8))
//                        .setMaterial(new Material()
//                                .setKD(0.5).setKS(0.5).setShininess(100)
//                                .setKr(new Double3(0.3, 0.3, 0.4))),
//
//                // Mur arrière sombre
//                new Plane(new Point(0, 0, -600), new Vector(0, 0, 1))
//                        .setEmission(new Color(8, 8, 12))
//                        .setMaterial(new Material()
//                                .setKD(0.7).setKS(0.3).setShininess(50)),
//
//                // Sphères décoratives plus visibles
//                new Sphere(30d, new Point(-200, -150, -200))
//                        .setEmission(new Color(15, 25, 35))
//                        .setMaterial(new Material()
//                                .setKD(0.4).setKS(0.6).setShininess(100)
//                                .setKt(new Double3(0.2, 0.3, 0.4))),
//
//                new Sphere(30d, new Point(200, -150, -200))
//                        .setEmission(new Color(35, 15, 25))
//                        .setMaterial(new Material()
//                                .setKD(0.4).setKS(0.6).setShininess(100)
//                                .setKt(new Double3(0.4, 0.2, 0.3))),
//
//                new Sphere(25d, new Point(0, -150, -100))
//                        .setEmission(new Color(25, 35, 15))
//                        .setMaterial(new Material()
//                                .setKD(0.4).setKS(0.6).setShininess(100)
//                                .setKt(new Double3(0.3, 0.4, 0.2)))
//        );
//
//        // === ÉCLAIRAGE MODÉRÉ === (3+ sources de lumière)
//        scene.setAmbientLight(new AmbientLight(new Color(10, 10, 15)));
//
//        // Lumière principale modérée
//        scene.lights.add(new SpotLight(new Color(200, 220, 250), new Point(0, 300, 100), new Vector(0, -1, -0.8))
//                .setKl(0.0005).setKq(0.000005));
//
//        // Lumière latérale gauche douce
//        scene.lights.add(new SpotLight(new Color(150, 180, 200), new Point(-250, 150, 100), new Vector(1, -0.3, -0.8))
//                .setKl(0.001).setKq(0.00001));
//
//        // Lumière latérale droite douce
//        scene.lights.add(new SpotLight(new Color(180, 150, 200), new Point(250, 150, 100), new Vector(-1, -0.3, -0.8))
//                .setKl(0.001).setKq(0.00001));
//
//        // Lumière d'arrière-plan très douce
//        scene.lights.add(new DirectionalLight(new Color(30, 40, 50), new Vector(0, -0.5, -1)));
//
//        // === RENDU SANS EFFETS ===
//        cameraBuilder
//                .setLocation(new Point(0, 0, 300))
//                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
//                .setVpDistance(300).setVpSize(400, 400)
//                .setResolution(500, 500)
//                // .setAntiAliasing(false) // Désactiver anti-aliasing
//                // .setGlossyReflection(false) // Désactiver glossy
//                .build()
//                .renderImage()
//                .writeToImage("DiamondImage_NoEffects");
//
//        // === RENDU AVEC EFFETS ===
//        cameraBuilder
//                .setLocation(new Point(0, 0, 300))
//                .setDirection(new Point(0, -50, -300), Vector.AXIS_Y)
//                .setVpDistance(300).setVpSize(400, 400)
//                .setResolution(500, 500)
//                // .setAntiAliasing(true) // Activer anti-aliasing
//                // .setAntiAliasingRays(81) // 9x9 grid (ou plus pour la qualité finale)
//                // .setGlossyReflection(true) // Activer glossy reflection
//                // .setGlossyRays(100) // Nombre de rayons pour l'effet glossy
//                .build()
//                .renderImage()
//                .writeToImage("DiamondImage_WithEffects");
//    }
}
