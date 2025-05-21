package lighting;

import primitives.Color;

/**
 * AmbientLight class represents the ambient light in the scene
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class AmbientLight extends Light {


    /**
     * A constant representing the absence of ambient light.
     */
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK);


    /**
     * Constructor for the AmbientLight class.
     * calls the constructor of the superclass Light
     *
     * @param intensity the intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        super(intensity);
    }




}
