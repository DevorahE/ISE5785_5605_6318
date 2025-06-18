package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import primitives.TargetArea;

import java.util.LinkedList;
import java.util.List;

/**
 * Scene class represents a scene in the 3D space
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Scene {
    /**
     * Name of the scene
     */
    public String name;

    /**
     * Background color of the scene
     */
    public Color background = Color.BLACK;

    /**
     * Ambient light of the scene
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * Geometries in the scene
     */
    public Geometries geometries = new Geometries();

    /**
     * List of light sources in the scene
     */
    public List<LightSource> lights = new LinkedList<>();
    /**
     * Defines the sampling pattern used for generating points on a target area in the scene.
     */
    public TargetArea.SamplingPattern samplingPattern = TargetArea.SamplingPattern.JITTERED;

    /**
     * Scene setter
     * @return the scene
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * Constructor for Scene
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Scene setter
     * @param background the background color of the scene
     * @return the scene
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Scene setter
     * @param ambientLight the ambient light of the scene
     * @return the scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Scene setter
     * @param geometries the geometries in the scene
     * @return the scene
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
    /**
     * Sets the sampling pattern to be used for generating points on a target area in the rendered scene.
     *
     * @param samplingPattern the sampling pattern to use for distributing points
     *                        (e.g., RANDOM, GRID, JITTERED)
     * @return this Scene object for method chaining
     */
    public Scene setSamplingPattern(TargetArea.SamplingPattern samplingPattern) {
        this.samplingPattern = samplingPattern;
        return this;
    }

}
