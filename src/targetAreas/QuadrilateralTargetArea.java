package targetAreas;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * A subclass of TargetArea representing a rectangular (quadrilateral) target area.
 * This target area is defined by its height and width and is capable of generating
 * sampling points using different patterns such as RANDOM, GRID, or JITTERED.
 */
public class QuadrilateralTargetArea extends TargetArea {

    /**
     * Represents the height of the quadrilateral target area.
     */
    private final double height;

    /**
     * Represents the width of the quadrilateral target area.
     */
    private final double width;

    /**
     * Constructs a QuadrilateralTargetArea object with specified dimensions, orientation,
     * target center, sampling details, and sampling pattern.
     *
     * @param height the height of the quadrilateral target area
     * @param width the width of the quadrilateral target area
     * @param x the vector defining the X-axis direction of the quadrilateral plane
     * @param normal the normal vector perpendicular to the quadrilateral plane
     * @param targetCenter the central point of the quadrilateral target area
     * @param numSamples the number of sample points to be generated on the quadrilateral target area
     * @param samplingPattern the pattern used to define the sampling distribution on the quadrilateral target area
     */
    public QuadrilateralTargetArea(double height, double width, Vector x, Vector normal, Point targetCenter, int numSamples, SamplingPattern samplingPattern) {
        super(x, normal, targetCenter, numSamples, samplingPattern);
        this.height = height;
        this.width = width;
    }

    /**
     * Constructs a QuadrilateralTargetArea object based on a specified height and width, a ray,
     * a distance along the ray, the number of samples, and a sampling pattern for generating points.
     *
     * @param height the height of the quadrilateral target area
     * @param width the width of the quadrilateral target area
     * @param ray the ray defining the direction and location of the target area
     * @param distance the distance along the ray to the center of the target area
     * @param numSamples the number of sample points to be generated on the target area
     * @param samplingPattern the pattern used to generate sample points on the target area
     */
    public QuadrilateralTargetArea(double height, double width, Ray ray, double distance, int numSamples, SamplingPattern samplingPattern){
        super(ray, distance, numSamples, samplingPattern);
        this.height = height;
        this.width = width;
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
            double x = (RANDOM.nextDouble() - 0.5) * width;
            double y = (RANDOM.nextDouble() - 0.5) * height;
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
        double aspectRatio = width / height;

        int gridSizeY = (int) Math.round(Math.sqrt(numSamples / aspectRatio));
        int gridSizeX = (int) Math.round((double) numSamples / gridSizeY);

        double cellWidth = width / gridSizeX;
        double cellHeight = height / gridSizeY;

        for (int i = 0; i < gridSizeX && points.size() < numSamples; i++) {
            for (int j = 0; j < gridSizeY && points.size() < numSamples; j++) {
                double x = -width / 2 + cellWidth * (i + 0.5);
                double y = -height / 2 + cellHeight * (j + 0.5);

                if (samplingPattern == SamplingPattern.JITTERED) {
                    double jitterX = (RANDOM.nextDouble() - 0.5) * cellWidth * 0.8;
                    double jitterY = (RANDOM.nextDouble() - 0.5) * cellHeight * 0.8;
                    x += jitterX;
                    y += jitterY;
                }

                Point targetPoint = targetCenter;
                if (!Util.isZero(x)) targetPoint = targetPoint.add(xVec.scale(x));
                if (!Util.isZero(y)) targetPoint = targetPoint.add(yVec.scale(y));
                points.add(targetPoint);
            }
        }
        return points;
    }
}