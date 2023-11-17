package cypher.enforcers.utilities;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import cypher.enforcers.commands.Command;
import cypher.enforcers.commands.SwitchToDarkMode;
import cypher.enforcers.commands.SwitchToHighContrastMode;
import cypher.enforcers.commands.SwitchToLightMode;
import cypher.enforcers.commands.managers.ThemeSwitcher;
import cypher.enforcers.data.implementations.*;
import cypher.enforcers.data.security.mappers.AccountDTOMapper;
import cypher.enforcers.data.security.mappers.CodeDTOMapper;
import cypher.enforcers.data.security.mappers.UserDTOMapper;
import cypher.enforcers.data.spis.*;
import cypher.enforcers.models.AccountModel;
import cypher.enforcers.models.CodeModel;
import cypher.enforcers.models.UserModel;
import cypher.enforcers.views.accountview.AccountView;
import cypher.enforcers.views.codeview.CodeView;
import cypher.enforcers.views.themes.Theme;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.views.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 This class contains utility methods used amongst the controllers.
 */
public class Utilities {

    /**
     * Private constructor to create utilities.
     * No reason to make an instance of this object, instead use the static
     * methods provided.
     * <br>
     * Mainly here to avoid warnings.
     */
    private Utilities() {

    }

    /** Logger for the utility class. */
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    /**
     * Path to the logback configuration file (relative to the
     * resources folder).
     */
    private static final String PATH_TO_LOGBACK_CONFIG = "logback.xml";

    /**
     * Adjust the theme for this application.
     *
     * @param theme The new theme to switch to.
     * @throws IOException if any errors occur while loading in the views.
     * @throws NullPointerException If there's any missing data that
     * prevents the theme from being changed, such as icons.
     */
    public static void adjustTheme(Theme theme) throws IOException, NullPointerException {
        if (theme == null) {
            logger.debug("Cannot switch theme because it's null. Aborting request.");
            return;
        }

        logger.debug("Switching theme to {}.", theme);
        ThemeSwitcher switcher = getThemeSwitcher(theme);
        switcher.switchTheme();
    }

    /**
     * Create and return a ThemeSwitcher based on the theme provided.
     * Usually called when the theme of the application is being changed.
     *
     * @param theme Name of the theme.
     * @return The ThemeSwitcher ready to change all the themes.
     * @throws IOException if any errors occur while loading in the views.
     * @throws NullPointerException If there's any missing data that
     * prevents any of the views from being created.
     */
    private static ThemeSwitcher getThemeSwitcher(Theme theme) throws IOException, NullPointerException {
        List<View> views = List.of(
                HomePageView.getInstance(),
                SignUpView.getInstance(),
                SignInView.getInstance(),
                AccountView.getInstance(),
                AddAccountView.getInstance(),
                CodeView.getInstance(),
                SettingsView.getInstance()
        );

        Command command;

        switch (theme) {
            case HIGH_CONTRAST -> command = new SwitchToHighContrastMode(views);
            case DARK -> command = new SwitchToDarkMode(views);
            default -> command = new SwitchToLightMode(views);
        }

        return new ThemeSwitcher(command);
    }

    /**
     * Load a file from the resource folder in the format of
     * a URL.
     *
     * @param path Path to the file relative to the resource folder.
     * @return The URL to the file relative to the resource's folder.
     * Never null or empty.
     * @throws NullPointerException If the resource can't be found.
     */
    public static URL loadFileByURL(String path) throws NullPointerException {
        logger.debug("Attempting to load file from resource: /cypher/enforcers/" + path);
        return Objects.requireNonNull(
                Utilities.class.getResource("/cypher/enforcers/" + path)
        );
    }

    /**
     * Load a file from the resource folder in the format of
     * a InputStream.
     *
     * @param path Path to the file relative to the resource folder.
     * @return The input stream to the file relative to the resource's
     *         folder.
     *         Never null or pointing to a file that doesn't exist.
     * @throws NullPointerException If the resource can't be found.
     */
    public static InputStream loadFileByInputStream(String path) throws NullPointerException {
        logger.debug("Attempting to load file (as input stream) from resource: /cypher/enforcers/" + path);
        return Objects.requireNonNull(
                Utilities.class.getResourceAsStream("/cypher/enforcers/" + path)
        );
    }

    /**
     * Get the absolute path to the parent directory that this application
     * is being executed in.
     *
     * @return The path as a string.
     */
    public static String getParentDirectory() {
        return Paths.get("").toAbsolutePath().toString();
    }

    /**
     * Create and copy a file from the resources folder if it doesn't
     * exist in the same directory in which this application is running.
     *
     * @param file Path of file (relative to the resources folder).
     * @throws NullPointerException When the file from resources can't be
     * found.
     */
    public static void copyResourceFileIf(String file) throws NullPointerException {
        /*
        The file variable only contains the path relative to the resources
        folder, but we just need the name. So we first load the file
        from resources, then use a utility method to extract the name from
        a URL.
         */

        // Needed for logging.
        URL url = null;

        try (InputStream inputStream = loadFileByInputStream(file)) {
            url = loadFileByURL(file);

            File fileToCreate = new File(
                    getParentDirectory() +
                    File.separator +
                    FilenameUtils.getName(url.getPath())
            );
            if (!fileToCreate.exists()) {
                Files.copy(inputStream, fileToCreate.toPath());
            }
        } catch (IOException e) {
            logger.warn("Failed to move file {} from resources. Cause: {}", url, e.toString());
        }
    }

    /**
     * Update the path to the account icon based on the social
     * media type by storing it in a Map where the key is the
     * name of the platform and the value is the path.
     *
     * @param icons Map to store the social media platform name
     *              as the key and path to the icon as the value.
     * @param discord Path to the icon of the Discord social media platform
     *                (relative to the resources folder).
     * @param github Path to the icon of the GitHub social media platform
     *               (relative to the resources folder).
     * @param google Path to the icon of the Google social media platform
     *               (relative to the resources folder).
     * @param shopify Path to the icon of the Shopify social media platform
     *                (relative to the resources folder).
     * @param defaultIcon Path to the icon of the default
     *                    social media platform (relative to the resources
     *                    folder).
     * @throws NullPointerException If any of the image files cannot
     * be found.
     */
    public static void updateIcons(
            Map<String, String> icons,
            String discord, String github,
            String google, String shopify,
            String defaultIcon
    ) throws NullPointerException {
        logger.trace("Updating icon for Discord from {} to {}.", icons.get("discord"), discord);
        icons.put("discord", loadFileByURL(discord).toExternalForm());

        logger.trace("Updating icon for Github from {} to {}.", icons.get("github"), github);
        icons.put("github", loadFileByURL(github).toExternalForm());

        logger.trace("Updating icon for Google from {} to {}.", icons.get("google"), google);
        icons.put("google", loadFileByURL(google).toExternalForm());

        logger.trace("Updating icon for Shopify from {} to {}.", icons.get("shopify"), shopify);
        icons.put("shopify", loadFileByURL(shopify).toExternalForm());

        logger.trace("Updating icon for default from {} to {}.", icons.get("default"), defaultIcon);
        icons.put("default", loadFileByURL(defaultIcon).toExternalForm());
    }

    /**
     * Prepare the user model.
     *
     * @param service The database service.
     * @return The new user model.
     */
    public static UserModel prepareUserModel(DatabaseService service) {
        UserDAO userDAO = new UserDAOImpl(service);
        UserRepository repository = new UserRepositoryImpl(userDAO);
        UserDTOMapper mapper = new UserDTOMapper();
        AuthenticationService authService = new AuthenticationServiceImpl(repository, mapper);
        return new UserModel(authService, service::disconnect);
    }

    /**
     * Prepare the account model.
     *
     * @param service The database service.
     * @return The new account model.
     */
    public static AccountModel prepareAccountModel(DatabaseService service) {
        AccountDAO accountDAO = new AccountDAOImpl(service);
        AccountRepository repository = new AccountRepositoryImpl(accountDAO);
        AccountDTOMapper mapper = new AccountDTOMapper();
        return new AccountModel(repository, mapper);
    }

    /**
     * Prepare the code model.
     *
     * @param service The database service.
     * @return The new code model.
     */
    public static CodeModel prepareCodeModel(DatabaseService service) {
        CodeDAO codeDAO = new CodeDAOImpl(service);
        CodeRepository repository = new CodeRepositoryImpl(codeDAO);
        CodeDTOMapper mapper = new CodeDTOMapper();
        return new CodeModel(repository, mapper);
    }

    /**
     * Prepare additional resources for this application such as
     * any event handlers, streams, resources, etc.
     *
     * @param stage The main window for this application.
     * @throws NullPointerException If the configuration file can't
     * be found from resources.
     * @throws IOException If any errors occur when loading the resource
     * file.
     * @throws JoranException If any errors occur during the configuration
     * process after the file has been loaded (such as unknown xml tags).
     */
    public static void prepare(Stage stage) throws NullPointerException,
            JoranException, IOException {
        configureLogger();
        registerExceptionHandler(stage);
    }

    /**
     * Register an event handler for when an uncaught exception occurs
     * in this application.
     *
     * @param stage The main stage for this application. Used to close
     *              any resources such as files, streams, connections, etc.
     */
    private static void registerExceptionHandler(Stage stage) {
        Thread thread = Thread.currentThread();

        if (thread.getName().equals("JavaFX Application Thread")) {
            thread.setUncaughtExceptionHandler(
                    (t, e) -> onException(stage, e)
            );
        }
    }

    /**
     * Handle method for when an uncaught exception occurs in this
     * application.
     *
     * @param stage The main stage for this application.
     * @param e The exception.
     */
    public static void onException(Stage stage, Throwable e) {
        logger.error("Error occurred, shutting down. Cause: ", e);
        stage.fireEvent(
                new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)
        );
        Platform.exit();
    }

    /**
     * Configure logger to use configuration file.
     *
     * @throws NullPointerException If the configuration file can't
     * be found from resources.
     * @throws IOException If any errors occur when loading the resource
     * file.
     * @throws JoranException If any errors occur during the configuration
     * process after the file has been loaded (such as unknown xml tags).
     */
    private static void configureLogger() throws NullPointerException,
            IOException, JoranException {
        logger.trace("Configuring logger...");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        try (InputStream configStream = Utilities.loadFileByInputStream(PATH_TO_LOGBACK_CONFIG)) {
            configurator.doConfigure(configStream); // loads logback file
        }
    }
}