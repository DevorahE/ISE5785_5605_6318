package renderer;

import primitives.*;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

public class Camera implements Cloneable {
    // Camera properties
    private Point location = Point.ZERO; // Camera position
    private Vector vTo = new Vector(0,0,-1); // Camera direction
    private Vector vUp = new Vector(0,1,0); // Camera up vector
    private Vector vRight = new Vector(1,0,0); // Camera right vector
    private double width = 0.0; // Width of the view plane
    private double height = 0.0; // Height of the view plane
    private double distance = 0.0; // Distance from the camera to the view plane

    public Ray constructRay(int nX, int nY, int j, int i){
        //todo
        return null;
    }
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i){
        //todo
        return null;
    }
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    private Camera(){
        // Private constructor to prevent instantiation
    }
    public Builder getBuilder() {
        return new Builder();
    }

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

            camera.vTo = vTo;
            camera.vUp = vUp;
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
        public Builder setVPSize(double width, double height) {
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
        public Builder setVPDistance(double distance) {
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
            if (camera.vTo.lengthSquared() != 1)
                throw new IllegalArgumentException("ERROR: THe vector vTo isn't normalized");
            if (camera.vUp.lengthSquared() != 1)
                throw new IllegalArgumentException("ERROR: THe vector vUp isn't normalized");
            if (camera.width == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "width");
            if (camera.height == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "height");
            if (camera.distance == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "distance");

            // Compute vRight if not yet calculated
            Vector vRight = camera.vTo.crossProduct(camera.vUp);
            if (vRight.lengthSquared() == 0)
                throw new IllegalArgumentException("ERROR: vTo and vUp cannot be parallel – cannot compute vRight.");
            camera.vRight = vRight.normalize();

            try {
                return (Camera)camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}

