package renderer;

import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import static java.awt.Color.*;

public class DepthOfFieldTest {

    /** Scene of the tests */
    private final Scene scene      = new Scene("Test scene");
    /** Camera builder of the tests */
    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(new Point(0, 0, 1000))
            .setDirection(Vector.AXIS_Y)
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(scene, RayTracerType.SIMPLE);


    @Test
    void focusTest() {
        scene.geometries
                .add(
                        //
                        new Sphere(50d, new Point(0, 0, -150)).setEmission(new Color(RED)),

                        //
                        new Sphere(30, new Point(-100, 0, -100)).setEmission(new Color(GREEN)),

                        //
                        new Sphere(30, new Point(100, 0, -200)).setEmission(new Color(BLUE))
                );
        camera
                .setRayTracer(scene, RayTracerType.SIMPLE) //
                .setResolution(1000, 1000) //
                .setMultithreading(4)
                .setDebugPrint(10)
                .build() //
                .renderImage() //
                .writeToImage("FocusEffectTest-withoutFocus");
        camera
                .setNumOfRaysDOF(100) //
                .setApertureWindow(200, 200) //
                .setDistanceFocalPlane(1150) //
                .setMultithreading(4)
                .setDebugPrint(10)
                .build() //
                .renderImage() //
                .writeToImage("FocusEffectTest-withFocus");

    }


    }
