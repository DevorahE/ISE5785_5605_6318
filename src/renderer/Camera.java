package renderer;

import primitives.*;
import renderer.sampling.*;
import scene.Scene;

import java.awt.geom.Point2D;
import java.util.List;
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

    // === super‐sampling fields ===
    private SamplingConfig samplingConfig = new SamplingConfig();
    private SuperSamplingBlackboard blackboard = new SuperSamplingBlackboard(samplingConfig);


    /**
     * Renders the image by casting rays through each pixel of the view plane.
     *
     * @return the Camera instance for method chaining
     */
    public Camera renderImage()
    {
        int nx = imageWriter.nX();
        int ny = imageWriter.nY();

        for (int i=0; i < nx; i++)
        {
            for(int j=0; j < ny; j++)
                castRay(j, i);
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

    /**
     * Casts a ray through a specific pixel on the view plane and writes the color to the image.
     *
     * @param column the pixel index in the X direction.
     * @param row    the pixel index in the Y direction.
     */
    private void castRay(int column, int row)
    {
        double pixelW = width / nX;
        double pixelH = height / nY;

        List<Point2D.Double> samples = blackboard.getSampleOffsets(pixelW, pixelH);
        Color pixelColor = new Color(0, 0, 0);

        for (Point2D.Double off : samples) {
            double xJ = (column - (nX - 1) / 2.0) * pixelW + off.x;
            double yI = (row - (nY - 1) / 2.0) * pixelH + off.y;

            Point pC = location.add(vTo.scale(distance));
            if (!isZero(xJ)) pC = pC.add(vRight.scale(xJ));
            if (!isZero(yI)) pC = pC.add(vUp.scale(-yI));

            Ray sampleRay = new Ray(location, pC.subtract(location));
            pixelColor = pixelColor.add(rayTracer.traceRay(sampleRay));
        }

        pixelColor = pixelColor.scale(1.0 / samples.size());
        imageWriter.writePixel(column, row, pixelColor);
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

    // Getters
    public Point getLocation() { return location; }
    public Vector getvTo() { return vTo; }
    public Vector getvUp() { return vUp; }
    public Vector getvRight() { return vRight; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDistance() { return distance; }

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
     * A builder class for the Camera class with support for transformations.
     */
    public static class Builder {
        private final Camera camera = new Camera();

        // Transformation intermediate data
        private Point baseLocation = null;
        private Vector baseVTo = null;
        private Vector baseVUp = null;
        private boolean hasTransformations = false;

        /**
         * Sets the camera location.
         */
        public Builder setLocation(Point location)
        {
            if (location == null)
                throw new IllegalArgumentException("ERROR: Camera location cannot be null.");
            camera.location = location;
            baseLocation = location;
            return this;
        }

        /**
         * Sets the camera direction using two orthogonal vectors: forward (vTo) and upward (vUp).
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
            baseVTo = camera.vTo;
            baseVUp = camera.vUp;
            return this;
        }

        /**
         * Sets the camera direction using a target point and an approximate up vector.
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
            baseVTo = camera.vTo;
            baseVUp = camera.vUp;
            return this;
        }

        /**
         * Sets the camera direction using a target point with default up vector.
         */
        public Builder setDirection(Point target) {
            return setDirection(target, new Vector(0, 1, 0));
        }

        /**
         * Sets the view plane size.
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
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("ERROR: VP distance must be positive.");
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the view plane.
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
         * Configure anti‐aliasing parameters.
         */
        public Builder setSamplingConfig(SamplingConfig config) {
            camera.samplingConfig = config;
            camera.blackboard = new SuperSamplingBlackboard(config);
            return this;
        }
        // ================= TRANSFORMATION METHODS =================

        /**
         * Rotates the camera around the Z-axis by the given angle in degrees.
         *
         * @param angleDegrees the rotation angle in degrees
         * @return this Builder object
         */
        public Builder rotateZ(double angleDegrees) {
            return rotate(angleDegrees, new Vector(0, 0, 1));
        }

        /**
         * Rotates the camera around an arbitrary axis by the given angle in degrees.
         *
         * @param angleDegrees the rotation angle in degrees
         * @param axis the rotation axis (will be normalized)
         * @return this Builder object
         */
        public Builder rotate(double angleDegrees, Vector axis) {
            if (axis == null)
                throw new IllegalArgumentException("ERROR: Rotation axis cannot be null.");
            if (axis.lengthSquared() == 0)
                throw new IllegalArgumentException("ERROR: Rotation axis cannot be zero vector.");
            if (camera.vTo == null || camera.vUp == null)
                throw new IllegalArgumentException("ERROR: Camera direction must be set before rotation.");

            double angleRadians = Math.toRadians(angleDegrees);
            axis = axis.normalize();

            // Apply Rodrigues' rotation formula
            camera.vTo = rotateVector(camera.vTo, axis, angleRadians);
            camera.vUp = rotateVector(camera.vUp, axis, angleRadians);

            hasTransformations = true;
            return this;
        }


        /**
         * Helper method to rotate a vector using Rodrigues' rotation formula.
         */
        private Vector rotateVector(Vector v, Vector axis, double angleRadians) {
            double cosTheta = Math.cos(angleRadians);
            double sinTheta = Math.sin(angleRadians);

            // Rodrigues' rotation formula: v' = v*cos(θ) + (k×v)*sin(θ) + k*(k·v)*(1-cos(θ))
            Vector crossProduct = axis.crossProduct(v);
            double dotProduct = axis.dotProduct(v);

            return v.scale(cosTheta)
                    .add(crossProduct.scale(sinTheta))
                    .add(axis.scale(dotProduct * (1 - cosTheta)));
        }
        /**
         * Fait orbiter la caméra autour d'un point central à une distance donnée
         *
         * @param centre le point autour duquel orbiter
         * @param angleDegres l'angle de rotation en degrés
         * @param axe l'axe de rotation (généralement AXIS_Y pour rotation horizontale)
         * @return this Builder object
         */
        public Builder orbitAround(Point centre, double angleDegres, Vector axe) {
            if (centre == null)
                throw new IllegalArgumentException("ERREUR: Le centre d'orbite ne peut pas être null.");
            if (axe == null)
                throw new IllegalArgumentException("ERREUR: L'axe de rotation ne peut pas être null.");
            if (axe.lengthSquared() == 0)
                throw new IllegalArgumentException("ERREUR: L'axe de rotation ne peut pas être un vecteur zéro.");
            if (camera.location == null)
                throw new IllegalArgumentException("ERREUR: La position de la caméra doit être définie avant l'orbite.");

            double angleRadians = Math.toRadians(angleDegres);
            axe = axe.normalize();

            // Calculer le vecteur de la position actuelle vers le centre
            Vector vecToCentre = centre.subtract(camera.location);

            // Faire tourner ce vecteur autour de l'axe
            Vector vecToCentreRotated = rotateVector(vecToCentre, axe, angleRadians);

            // Calculer la nouvelle position
            camera.location = centre.subtract(vecToCentreRotated);

            // Optionnel : faire regarder la caméra vers le centre
            Vector direction = centre.subtract(camera.location);
            if (direction.lengthSquared() > 0) {
                camera.vTo = direction.normalize();
                // Recalculer vRight et vUp
                camera.vRight = camera.vTo.crossProduct(Vector.AXIS_Y).normalize();
                camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            }

            hasTransformations = true;
            return this;
        }
        /**
         * Builds and returns a new Camera object with the specified properties.
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
                throw new IllegalArgumentException("ERROR: The vector vTo isn't normalized");
            if (!isZero(camera.vUp.lengthSquared() - 1 ))
                throw new IllegalArgumentException("ERROR: The vector vUp isn't normalized");
            if (camera.width == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "width");
            if (camera.height == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "height");
            if (camera.distance == 0)
                throw new MissingResourceException(MISSING_DATA, CLASS_NAME, "distance");
            if  (camera.nX <= 0 || camera.nY <= 0)
                throw new IllegalArgumentException("ERROR: The resolution cannot be negative");

            // Compute vRight
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