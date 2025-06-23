package renderer;


import primitives.*;
import org.junit.jupiter.api.Test;
import scene.Scene;
import targetAreas.TargetArea;

import static renderer.DiamondSceneSetUp.setUpDiamond;


public class Minip2Tests {

    private final Scene scene = new Scene("Test scene").setSamplingPattern(TargetArea.SamplingPattern.JITTERED);

    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
            .setRayTracer(scene, RayTracerType.SIMPLE);


    @Test
    void diamondSceneWithEffectsWithAcceleration() {

        setUpDiamond(scene);
        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .setNumOfRaysAA(9)
                .setNumOfRaysDOF(30)
                .setApertureWindow(20, 40)
                .setDistanceFocalPlane(500)
                .setMultithreading(-2)
                .setAdaptiveSuperSampling(2)
                .build()
                .renderImage()
                .writeToImage("diamond WithEffects WithAcceleration");

    }


    //1min46
    @Test
    void diamondSceneWithoutEffectsWithoutAcceleration() {

        setUpDiamond(scene);
        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .build()
                .renderImage()
                .writeToImage("diamond WithoutEffects WithoutAcceleration");

    }


    @Test
    void diamondSceneWithEffectsWithoutAcceleration() {

        setUpDiamond(scene);
        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .setNumOfRaysAA(9)
                .setNumOfRaysDOF(30)
                .setApertureWindow(20, 40)
                .setDistanceFocalPlane(500)
                .build()
                .renderImage()
                .writeToImage("diamond WithEffects WithoutAcceleration");

    }


    //30 sec
    @Test
    void diamondSceneWithoutEffectsWithAcceleration() {
        setUpDiamond(scene);
        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .setMultithreading(-2)
                .build()
                .renderImage()
                .writeToImage("diamond WithoutEffects WithAcceleration");

    }

    @Test
    void diamondSceneWithAperture() {

        setUpDiamond(scene);

        cameraBuilder
                .setLocation(new Point(0, 50, 200))
                .setDirection(new Point(0, -100, -350), Vector.AXIS_Y)
                .setVpDistance(300).setVpSize(600, 600)
                .setResolution(800, 800)
                .setMultithreading(-2)
                .setNumOfRaysDOF(30)
                .setApertureWindow(20, 40)
                .setDistanceFocalPlane(500)
                .build()
                .renderImage()
                .writeToImage("diamond Aperture");

    }

}
