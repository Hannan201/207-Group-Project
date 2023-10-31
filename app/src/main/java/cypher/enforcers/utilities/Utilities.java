package cypher.enforcers.utilities;

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
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.views.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 This class contains utility methods used amongst the controllers.
 */
public class Utilities {

    // Logger for the utility class.
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    /**
     * Adjust the theme for this application.
     *
     * @param theme The new theme to switch to.
     */
    public static void adjustTheme(Theme theme) {
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
     */
    private static ThemeSwitcher getThemeSwitcher(Theme theme) {
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
     */
    public static URL loadFileByURL(String path) {
        logger.debug("Attempting to load file from resource: " + path);
        return Objects.requireNonNull(
                Utilities.class.getResource(path)
        );
    }

    /**
     * Load a file from the resource folder in the format of
     * a InputStream.
     *
     * @param path Path to the file relative to the resource folder.
     */
    public static InputStream loadFileByInputStream(String path) {
        logger.debug("Attempting to load file (as input stream) from resource: " + path);
        return Objects.requireNonNull(
                Utilities.class.getResourceAsStream(path)
        );
    }

    /**
     * Get absolute path to parent directory that this application
     * is being executed in.
     *
     * @return The path as a string.
     */
    public static String getJarParentDirectory() {
        try {
            return new File(
                    Utilities.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            ).getParent();
        } catch (URISyntaxException e) {
            logger.warn("Unable to find parent directory of jar. Cause: ", e);
        }

        return null;
    }

    /**
     * Create and copy a file from the resources folder if it doesn't
     * exist in the same directory in which this application is running.
     *
     * @param file Path of file (relative to the resources folder).
     */
    public static void copyResourceFileIf(String file) {
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
                    Objects.requireNonNull(getJarParentDirectory()) +
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
     */
    public static void updateIcons(
            Map<String, String> icons,
            String discord, String github,
            String google, String shopify,
            String defaultIcon
    ) {
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
        return new UserModel(authService);
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
}