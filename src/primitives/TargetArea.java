package primitives;
import java.util.List;
import java.util.Random;

/**
 * Represents a target area for distributing rays in a geometric region.
 * Used for implementing antialiasing, soft shadows, and diffuse reflections.
 */
public abstract class TargetArea {

    /**
     * Enumeration defining different patterns for sampling points on the target area.
     */
    public enum SamplingPattern {
        /**
         * Random sampling with uniform distribution
         */
        RANDOM,
        /**
         * Regular grid sampling
         */
        GRID,
        /**
         * Grid sampling with jittered (randomized) positions
         */
        JITTERED
    }

    /**
     * The normal vector perpendicular to the target area's plane.
     * It defines the orientation of the target area in 3D space.
     */
    protected final Vector normal;

    /**
     * The central point of the target area in 3D space.
     */
    protected final Point targetCenter;

    /**
     * X-axis for the target plane
     */
    protected Vector xVec;

    /**
     * Y-axis for the target plane
     */
    protected Vector yVec;

    /**
     * The number of sample points to generate.
     */
    protected int numSamples;

    /**
     * The sampling pattern to use for generating points.
     */
    protected final SamplingPattern samplingPattern;

    /**
     * Random number generator for sampling methods.
     */
    static final Random RANDOM = new Random();

    /**
     * Constructs a TargetArea object with specified parameters including orientation,
     * sampling information, and target center.
     *
     * @param x the vector defining the X-axis direction of the target area plane
     * @param normal the normal vector perpendicular to the target area plane
     * @param targetCenter the central point of the target area
     * @param numSamples the number of sample points to be generated on the target area
     * @param samplingPattern the pattern used to define the sampling distribution on the target area
     */
    protected TargetArea(Vector x, Vector normal, Point targetCenter, int numSamples, SamplingPattern samplingPattern) {
        this.normal = normal;
        this.targetCenter = targetCenter;
        this.numSamples = numSamples;
        this.samplingPattern = samplingPattern;
        this.xVec = x;
        this.yVec = normal.crossProduct(xVec).normalize();
    }

    /**
     * Constructs a TargetArea object based on a ray, a specified distance along the ray,
     * the number of sampling points, and a sampling pattern to define the target area.
     *
     * @param ray the ray from which the target area's direction and location are derived
     * @param distance the distance along the ray to the center of the target area
     * @param numSamples the number of sample points to be generated on the target area
     * @param samplingPattern the pattern used to generate sample points on the target area
     */
    protected TargetArea(Ray ray, double distance, int numSamples, SamplingPattern samplingPattern) {
        this.normal = ray.getDirection();
        this.targetCenter = ray.getPoint(distance);
        this.numSamples = numSamples;
        this.samplingPattern = samplingPattern;
        Vector baseVector;
        // Choose the axis that is more perpendicular to the ray direction
        if (Math.abs(normal.dotProduct(Vector.AXIS_Y)) < Math.abs(normal.dotProduct(Vector.AXIS_X)))
            baseVector = Vector.AXIS_Y;
        else
            baseVector = Vector.AXIS_X;
        // Create an orthogonal coordinate system on the target area
        this.xVec = baseVector.crossProduct(normal).normalize();
        this.yVec = normal.crossProduct(xVec).normalize();
    }

    /**
     * Generates a list of points based on the defined sampling pattern,
     * number of samples, and other properties of the target area.
     *
     * @return a list of generated points within the target area
     */
    public abstract List<Point> generatePoints();
}