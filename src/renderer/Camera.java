package renderer;

import primitives.*;
import renderer.sampling.*;
import scene.Scene;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

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

    /** Amount of threads to use fore rendering image by the camera */
    private int threadsCount = 0;
    /**
     * Amount of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;
    /**
     * Debug print interval in seconds (for progress percentage)<br>
     * if it is zero - there is no progress output
     */
    private double printInterval = 0;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    /**
     * Number of super sampling rays for antialiasing algorithm.
     */
    private int numOfRaysAA = 1;

    /**
     * Number of super sampling rays for depth of field algorithm.
     */
    private int numOfRaysDOF = 1;

    /**
     * Camera's aperture window which depends on camera's aperture.
     */
    private TargetArea apertureWindow;

    /**
     * Focal plane distance from camera
     */
    private double distanceFocalPlane;

    /**
     * Defines the sampling pattern used for generating points on a target area in the camera view plane.
     */
    private TargetArea.SamplingPattern samplingPattern = TargetArea.SamplingPattern.JITTERED;

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
     * Constructs a beam of rays through a specific pixel on the view plane.
     *
     * @param nX the number of pixels along the X-axis (image width)
     * @param nY the number of pixels along the Y-axis (image height)
     * @param j the column index of the pixel (X-axis)
     * @param i the row index of the pixel (Y-axis)
     * @return a list of rays representing the beam through the specified pixel
     */
    public List<Ray> constructBeam(int nX, int nY, int j, int i) {
        Point pC = location.add(vTo.scale(distance));
        double rY = height / nY;
        double rX = width / nX;
        double yI = -(i - 0.5 * (nY - 1)) * rY;
        double xJ = (j - 0.5 * (nX - 1)) * rX;
        Point pIJ = pC;
        if (!Util.isZero(xJ))
            pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI))
            pIJ = pIJ.add(vUp.scale(yI));
        Ray mainRay = new Ray(location, pIJ.subtract(location));
        if (numOfRaysAA == 1)
            return List.of(mainRay);
        QuadrilateralTargetArea targetArea = new QuadrilateralTargetArea(rY, rX, vRight, vTo, pIJ, numOfRaysAA, samplingPattern);
        return mainRay.createBeam(targetArea);
    }

    private void castRay(int j, int i) {
        List<Ray> beamRays = constructBeam(nX, nY, j, i);
        Color pixelColor = Color.BLACK;
        for (Ray ray : beamRays) {
            if(apertureWindow != null && numOfRaysDOF > 1) {
                List<Ray> beamRaysDOF = ray.createBeamReverse(apertureWindow, distanceFocalPlane);
                Color rayColor = Color.BLACK;
                for (Ray rayDOF : beamRaysDOF)
                    rayColor = rayColor.add(rayTracer.traceRay(rayDOF));
                rayColor = rayColor.reduce(beamRaysDOF.size());
                pixelColor = pixelColor.add(rayColor);
            }

            else
                pixelColor = pixelColor.add(rayTracer.traceRay(ray));
        }
        imageWriter.writePixel(j, i, pixelColor.reduce(beamRays.size()));
        pixelManager.pixelDone();
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
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }
    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }
    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }

    /** This function renders image's pixel color map from the scene
     * included in the ray tracer object
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
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
         * Sets the distance from the camera to the focal plane.
         *
         * @param distanceFocalPlane the distance value
         * @return this builder
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setDistanceFocalPlane(double distanceFocalPlane) {
            if (Util.alignZero(distanceFocalPlane) < 0)
                throw new IllegalArgumentException("Distance must be positive");
            camera.distanceFocalPlane = distanceFocalPlane;
            return this;
        }

        /**
         * Sets the number of super sampling rays for antialiasing algorithm
         *
         * @param numOfRaysAA number of super sampling rays for antialiasing algorithm
         * @return this builder
         * @throws IllegalArgumentException if numOfRays is smaller than 1
         */
        public Builder setNumOfRaysAA(int numOfRaysAA) {
            if (numOfRaysAA < 1)
                throw new IllegalArgumentException("Number of super sampling rays must not be smaller than 1");
            camera.numOfRaysAA = numOfRaysAA;
            return this;
        }

        /**
         * Sets the number of super sampling rays for depth of field algorithm
         *
         * @param numOfRaysDOF number of super sampling rays for depth of field algorithm
         * @return this builder
         * @throws IllegalArgumentException if numOfRays is smaller than 1
         */
        public Builder setNumOfRaysDOF(int numOfRaysDOF) {
            if (numOfRaysDOF < 1)
                throw new IllegalArgumentException("Number of super sampling rays must not be smaller than 1");
            camera.numOfRaysDOF = numOfRaysDOF;
            return this;
        }

        /**
         * Sets the number of super sampling rays for depth of field algorithm
         *
         * @param height the height of the aperture window
         * @param width the width of the aperture window
         * @return this builder
         * @throws IllegalArgumentException if parameters are negative
         * @throws IllegalArgumentException if necessary components for aperture window are not initialized yet
         */
        public Builder setApertureWindow(double height, double width) {
            if (camera.vRight == null || camera.vTo == null || camera.location == null)
                throw new IllegalArgumentException("Must initialize other components of the camera before setting aperture window");
            if (height <= 0 || width <= 0)
                throw new IllegalArgumentException("Height and width must not be negative");
            camera.apertureWindow = new QuadrilateralTargetArea(height, width, camera.vRight, camera.vTo, camera.location, camera.numOfRaysDOF, camera.samplingPattern);
            return this;
        }


        /**
         * Sets the sampling pattern for the target area.
         *
         * @param pattern the sampling pattern to use, such as RANDOM, GRID, or JITTERED
         * @return this builder instance
         */
        public Builder setSamplingPattern(TargetArea.SamplingPattern pattern) {
            camera.samplingPattern = pattern;
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
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
             * <li>-2 - number of threads is number of logical processors less 2</li>
             * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
             * <li>0 - multi-threading is not activated</li>
             * <li>1 and more - literally number of threads</li>
         * </ul>
         * @param threads number of threads
         * @return builder object itself
        */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }
        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
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