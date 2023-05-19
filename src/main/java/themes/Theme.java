package themes;

public class Theme {
    private static final ThemeConfiguration configuration =
            ThemeConfiguration.LIGHT;

    public static void setConfiguration(
            ThemeConfiguration newConfiguration
    ) {
        configuration.update(newConfiguration);
    }

    public static ThemeConfiguration getConfiguration() {
        return configuration;
    }
}
