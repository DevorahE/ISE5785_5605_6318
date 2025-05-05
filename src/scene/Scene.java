package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

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

}
