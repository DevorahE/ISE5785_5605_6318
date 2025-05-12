package renderer;

import primitives.*;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * The Camera class represents a camera in a 3D space with the ability to
 * generate rays through pixels for rendering.
 * @author Devorah Wajs and Guila Czerniewicz
 */
public class Camera implements Cloneable {
    // Camera properties
    private Point location = Point.ZERO; // Camera position
    private Vector vTo = new Vector(0,0,-1); // Camera direction
    private Vector vUp = new Vector(0,1,0); // Camera up vector
    private Vector vRight = new Vector(1,0,0); // Camera right vector
    private double width = 0.0; // Width of the view plane
    private double height = 0.0; // Height of the view plane
    private double distance = 0.0; // Distance from the camera to the view plane

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int nX = 1;
    private int nY = 1;

    /**
     * Renders the image by casting rays through each pixel of the view plane.
     * This method is currently not implemented and throws an UnsupportedOperationException.
     *
     * @return the Camera instance for method chaining
     * @throws UnsupportedOperationException if the method is called
     */
    public Camera renderImage()
    {
        int nx = imageWriter.nX();
        int ny = imageWriter.nY();

        for (int i=0; i < nx; i++)
        {
            for(int j=0; j < ny; j++)
                castRay(nx, ny, j, i);
        }
        return this;
    }

    /**
     * Prints a grid on the view plane for debugging purposes.
     * @param interval
     * @param color
     * @return this
     */
    public Camera printGrid(int interval, Color color)
    {
        for(int i=0; i < imageWriter.nX(); i++)
        {
            for(int j=0; j < imageWriter.nY(); j++)
            {
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(i, j, color);
            }
        }
        return this ;
    }

    /**
     * Writes the rendered image to a file.
     * @param nameFile
     * @return this
     */
    public Camera writeToImage(String nameFile)
    {
        imageWriter.writeToImage(nameFile);
        return this ;
    }

    private void castRay(int nx, int ny, int column, int row)
    {
        Ray ray = constructRay(nx, ny, column, row);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(column, row, color);
    }

    /**
     * Constructs a ray through a specific pixel on the view plane.
     *
     * @param nX the number of pixels in the X direction.
     * @param nY the number of pixels in the Y direction.
     * @param j  the pixel index in the X direction.
     * @param i  the pixel index in the Y direction.
     * @return the constructed {@link Ray}.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {

        // Calculate the center point of the view plane
        Point pC = location.add(vTo.scale(distance));

        // Calculate the size of each pixel in the view plane
        double rX =(width / (double)nX);
        double rY = (height / (double)nY);

        // Calculate the offset of the current pixel from the center
        double xJ = (j - (nX - 1) / 2d) * rX;
        double yI = -(i - (nY - 1) / 2d) * rY;

        // Initialize the intersection point of the ray with the view plane
        Point pIJ = pC;

        // Adjust the intersection point based on the pixel offset
        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(yI));
        }

        // Calculate the direction vector of the ray from the camera location to the pixel
        Vector vIJ = pIJ.subtract(location);

        // Return the constructed ray
        return new Ray(location, vIJ);


    }

    /**
     * Returns the location of the camera in the 3D space.
     *
     * @return the location of the camera.
     */
    public Point getLocation() {
        return location;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvRight() {
        return vRight;
    }
    /**
     * Returns the width of the view plane.
     *
     * @return the width of the view plane.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the view plane.
     *
     * @return the height of the view plane.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the distance of the camera to the view plane.
     *
     * @return the distance to the view plane.
     */
    public double getDistance() {
        return distance;
    }

    private Camera(){
        // Private constructor to prevent instantiation
    }

    /**
     * Returns a new {@link Builder} for constructing a {@code Camera} instance.
     *
     * @return a new {@link Builder}.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * A builder class for the Camera class.
     */
    public static class Builder {
        /// initialization of camera here; no need of constructor
        private final Camera camera = new Camera();


        /**
         * Sets the camera location.
         *
         * @param location the camera location
         * @return the builder instance
         * @throws IllegalArgumentException if the location is null
         */
        public Builder setLocation(Point location)
        {
            if (location == null)
                throw new IllegalArgumentException("ERROR: Camera location cannot be null.");
            camera.location = location;
            return this;
        }


        /// -------------------3 setDirection functions--------------------

        /**
         * Sets the camera direction using two orthogonal vectors: forward (vTo) and upward (vUp).
         *
         * @param vTo the forward direction vector
         * @param vUp the upward direction vector
         * @return this Builder object
         * @throws IllegalArgumentException if vectors are null, zero, or not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null)
                throw new IllegalArgumentException("ERROR: vTo and vUp must not be null.");
            if (vTo.lengthSquared() == 0 || vUp.lengthSquared() == 0)
                throw new IllegalArgumentException("ERROR: vTo and vUp must not be zero vectors.");
            if (!isZero(Math.abs(vTo.dotProduct(vUp))))
                throw new IllegalArgumentException("ERROR: vTo and vUp must be orthogonal.");

            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }


        /**
         * Sets the camera direction using a target point and an approximate up vector.
         * Calculates the correct vTo, vRight, and vUp vectors automatically.
         *
         * @param target the point the camera is directed toward
         * @param approxUp an approximate up direction vector (not necessarily orthogonal)
         * @return this Builder object
         * @throws IllegalArgumentException if any argument is null or if p0 has not been set
         */
        public Builder setDirection(Point target, Vector approxUp) {
            if (camera.location == null)
                throw new IllegalArgumentException("ERROR: Camera position must be set before calling setDirection.");
            if (target == null || approxUp == null)
                throw new IllegalArgumentException("ERROR: Target point and approxUp vector must not be null.");

            Vector vTo = target.subtract(camera.location).normalize();
            Vector vRight = vTo.crossProduct(approxUp).normalize();
            Vector vUp = vRight.crossProduct(vTo).normalize();

            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }


        /**
         * Sets the camera direction using a target point.
         * Assumes the default "up" direction is the positive Y axis (0,1,0).
         *
         * @param target the point the camera is directed toward
         * @return this Builder object
         * @throws IllegalArgumentException if the target is null or if p0 has not been set
         */
        public Builder setDirection(Point target) {
            return setDirection(target, new Vector(0, 1, 0));
        }

        /// ------------------------------------------------------------------------------------------


        /**
         * Sets the view plane size.
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return this Builder object
         * @throws IllegalArgumentException if width or height is not positive
       */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("ERROR: VP size must be positive.");
            camera.width = width;
            camera.height = height;
            return this;
        }


        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance from the camera to the view plane
         * @return this Builder object
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("ERROR: VP distance must be positive.");
            camera.distance = distance;
            return this;
        }


        /**
         * Sets the resolution of the view plane.
         *
         * @param nx the number of pixels in the x direction
         * @param ny the number of pixels in the y direction
         * @return this Builder object
         * @throws IllegalArgumentException if nx or ny is not positive
         */
        public Builder setResolution(int nx,int ny){
            if (nx <= 0 || ny <= 0)
                throw new IllegalArgumentException("ERROR: Resolution must be positive.");
            camera.nX = nx;
            camera.nY = ny;
            return this;
        }



        /**
         * Set the ray tracer
         * @param scene,
         * @param rayTracerType
         * @return the camera builder
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            if (scene == null || rayTracerType == null)
                throw new IllegalArgumentException("ERROR: Scene and RayTracerType must not be null.");
            if(rayTracerType == RayTracerType.SIMPLE)
                camera.rayTracer = new SimpleRayTracer(scene);
            else
                camera.rayTracer = null;
            return this;
        }



        /**
         * Builds and returns a new Camera object with the specified properties.
         *
         * @return a new Camera object
         * @throws IllegalStateException if the camera is not properly configured
         */
        public Camera build()  {
            String MISSING_DATA = "ERROR: Camera setup incomplete: missing rendering data.";
            String CLASS_NAME = "Camera";

            if (camera.location == null)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "location");
            if (camera.vTo == null)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "vTo");
            if (camera.vUp == null)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "vUp");
            if (!isZero(camera.vTo.lengthSquared() - 1 ))
                throw new IllegalArgumentException("ERROR: THe vector vTo isn't normalized");
            if (!isZero(camera.vUp.lengthSquared() - 1 ))
                throw new IllegalArgumentException("ERROR: THe vector vUp isn't normalized");
            if (camera.width == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "width");
            if (camera.height == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "height");
            if (camera.distance == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "distance");
            if  (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("ERROR: The resolution cannot be negative");

            // Compute vRight if not yet calculated
            Vector vRight = camera.vTo.crossProduct(camera.vUp);
            if (vRight.lengthSquared() == 0)
                throw new IllegalArgumentException("ERROR: vTo and vUp cannot be parallel – cannot compute vRight.");
            camera.vRight = vRight.normalize();

            if(camera.rayTracer == null)
                camera.rayTracer = new SimpleRayTracer(null);

            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
            try {
                return (Camera)camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}

