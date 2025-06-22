package targetAreas;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a circular target area for distributing rays in a 3D space.
 * This class extends the abstract TargetArea class and defines a circular region
 * characterized by its radius, orientation, and center position. It provides
 * methods for generating sampling points on its surface using different patterns.
 */
public class CircleTargetArea extends TargetArea {
    /**
     * The radius of the target area.
     */
    private final double radius;

    /**
     * The ratio of the area of a square to the area of a circle with the same diameter.
     * <p>
     * This constant is calculated as {@code 4.0 / Math.PI}, which approximates to 1.27324.
     */
    private static final double SQUARE_TO_CIRCLE_RATIO = 4.0 / Math.PI;

    /**
     * Constructs a CircleTargetArea object with a specified radius, orientation,
     * center position, and sampling details. This represents a circular target
     * area in 3D space.
     *
     * @param radius the radius of the circular target area
     * @param x the vector defining the X-axis direction of the target area plane
     * @param normal the normal vector perpendicular to the target area plane
     * @param targetCenter the central point of the target area
     * @param numSamples the number of sample points to be generated on the target area
     * @param samplingPattern the pattern used to define the sampling distribution on the target area
     */
    public CircleTargetArea(double radius, Vector x, Vector normal, Point targetCenter, int numSamples, SamplingPattern samplingPattern) {
        super(x, normal, targetCenter, numSamples, samplingPattern);
        this.radius = radius;
    }

    /**
     * Constructs a CircleTargetArea object, representing a circular target area in 3D space,
     * defined by its radius, orientation, center position, and sampling details.
     *
     * @param radius the radius of the circular target area
     * @param ray the ray defining the orientation and position of the target area
     * @param distance the distance along the ray to the center of the target area
     * @param numSamples the number of sampling points to be generated on the target area
     * @param samplingPattern the sampling pattern to use for generating points on the target area
     */
    public CircleTargetArea(double radius, Ray ray, double distance, int numSamples, SamplingPattern samplingPattern){
        super(ray, distance, numSamples, samplingPattern);
        this.radius = radius;
    }

    @Override
    public List<Point> generatePoints() {
        // Generate points according to the specified sampling pattern
        return samplingPattern == SamplingPattern.RANDOM ? generateRandomPoints()
                : generateGridPoints();
    }

    /**
     * Generates points using a random sampling pattern.
     */
    private List<Point> generateRandomPoints() {
        List<Point> points = new LinkedList<>();
        for (int i = 0; i < numSamples; i++) {
            double theta = RANDOM.nextDouble() * 2 * Math.PI;
            double r = Math.sqrt(RANDOM.nextDouble()) * radius;
            double x = r * Math.cos(theta);
            double y = r * Math.sin(theta);
            Point targetPoint = targetCenter;
            if (!Util.isZero(x)) targetPoint = targetPoint.add(xVec.scale(x));
            if (!Util.isZero(y)) targetPoint = targetPoint.add(yVec.scale(y));
            points.add(targetPoint);
        }

        return points;
    }

    /**
     * Generates points using a grid sampling pattern.
     * Adding jitter when chosen
     */
    private List<Point> generateGridPoints() {
        List<Point> points = new LinkedList<>();
        int adjustedNumSamples = (int) (numSamples * SQUARE_TO_CIRCLE_RATIO);
        int gridSize = (int) Math.ceil(Math.sqrt(adjustedNumSamples));
        double cellSize = 2 * radius / gridSize;
        for (int i = 0; i < gridSize && points.size() < numSamples; i++) {
            for (int j = 0; j < gridSize && points.size() < numSamples; j++) {
                double x = -radius + cellSize * (i + 0.5);
                double y = -radius + cellSize * (j + 0.5);
                if (samplingPattern == SamplingPattern.JITTERED) {
                    double jitterX = (RANDOM.nextDouble() - 0.5) * cellSize * 0.8;
                    double jitterY = (RANDOM.nextDouble() - 0.5) * cellSize * 0.8;
                    x += jitterX;
                    y += jitterY;
                }
                if (x * x + y * y <= radius * radius) {
                    Point targetPoint = targetCenter;
                    if (!Util.isZero(x)) targetPoint = targetPoint.add(xVec.scale(x));
                    if (!Util.isZero(y)) targetPoint = targetPoint.add(yVec.scale(y));
                    points.add(targetPoint);
                }
            }
        }

        return points;
    }
}