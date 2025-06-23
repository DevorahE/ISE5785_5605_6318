package renderer;

import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static renderer.DiamondSceneSetUp.setUpDiamond;

public class DiamondVideoTest {
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);

    @Test
    void diamondVideoImages() {
        setUpDiamond(scene);

        // Define the center point of the scene (what the camera should orbit around)
        Point sceneCenter = new Point(0, -100, -350);

        // Number of frames for video (adjust based on desired video length and frame rate)
        int totalFrames = 120;

        cameraBuilder
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)

                // Set initial camera position before orbiting
                .setMultithreading(-2);

        // Generate multiple images with camera orbiting around the scene
        for (int i = 0; i < totalFrames; i++) {

            // Calculate orbit angle: complete 360° rotation over all frames
            // This creates smooth circular motion for video
            double orbitAngle = (360.0 / totalFrames) * i;

            cameraBuilder
                    // Make camera orbit around scene center at calculated angle
                    // Using Y-axis for horizontal circular motion
                    .setLocation(new Point(0, 50, 200))
                    .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                    .orbitAround(sceneCenter, orbitAngle, Vector.AXIS_Y)

                    // Build camera, render and save image
                    .build()
                    .renderImage()
                    // Use zero-padded numbering for proper video sequence ordering
                    .writeToImage(String.format("Diamond_Video_Frame_%04d", i));
        }

    }


}
