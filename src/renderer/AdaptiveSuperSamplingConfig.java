package renderer;

/**
 * Configuration class for Adaptive Super Sampling (ASS) in a ray tracing renderer.
 * <p>
 * Adaptive Super Sampling is used to improve image quality by recursively sampling
 * pixel areas where color differences exceed a certain threshold, helping reduce aliasing
 * while minimizing redundant computations.
 */
public class AdaptiveSuperSamplingConfig {

    /**
     * Indicates whether Adaptive Super Sampling is enabled.
     * It is set to true if {@code maxDepth > 0}.
     */
    public final boolean enabled;

    /**
     * The maximum recursion depth for Adaptive Super Sampling.
     * A higher depth allows finer detail at the cost of performance.
     */
    public final int maxDepth;

    /**
     * The color threshold used to decide whether further sampling is needed.
     * If the color difference between samples exceeds this threshold,
     * additional sub-samples will be taken.
     */
    public final double colorThreshold;

    /**
     * Constructs a new AdaptiveSuperSamplingConfig with the given parameters.
     *
     * @param depth     the maximum recursion depth for sampling (set 0 to disable)
     * @param threshold the color difference threshold for triggering more sampling
     */
    public AdaptiveSuperSamplingConfig(int depth, double threshold) {
        this.enabled = depth > 0;
        this.maxDepth = depth;
        this.colorThreshold = threshold;
    }
    /**
     * A static configuration representing Adaptive Super Sampling being disabled.
     * It has a depth of 0 but retains a threshold value (ignored when disabled).
     */
    public static final AdaptiveSuperSamplingConfig DISABLED = new AdaptiveSuperSamplingConfig(0, 0.1);
}
