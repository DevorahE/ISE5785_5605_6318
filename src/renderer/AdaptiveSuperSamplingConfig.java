package renderer;

public class AdaptiveSuperSamplingConfig {

    public final boolean enabled;
    public final int maxDepth;
    public final double colorThreshold;

    public AdaptiveSuperSamplingConfig(int depth, double threshold) {
        this.enabled = depth > 0;
        this.maxDepth = depth;
        this.colorThreshold = threshold;
    }

    public static final AdaptiveSuperSamplingConfig DISABLED = new AdaptiveSuperSamplingConfig(0, 0.1);
}
