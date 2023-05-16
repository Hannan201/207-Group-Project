package themes;

public class Theme {
    private static ThemeConfiguration configuration =
            ThemeConfiguration.LIGHT;

    public static void setConfiguration(
            ThemeConfiguration newConfiguration
    ) {
        configuration = newConfiguration;
    }

    public static ThemeConfiguration getConfiguration() {
        return configuration;
    }
}
