public class Resources {
    public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        this(assets, metrics, config, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO);
    }

    public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config,
            CompatibilityInfo compatInfo) {
        mAssets = assets;
        mMetrics.setToDefaults();
        if (compatInfo != null) {
            mCompatibilityInfo = compatInfo;
        }
        updateConfiguration(config, metrics);
        assets.ensureStringBlocks();
    }