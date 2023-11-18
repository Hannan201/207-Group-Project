package cypher.enforcers.uberjar;

import cypher.enforcers.Launcher;

/**
 * This class is used as a Launcher if this application is being run
 * as an Uber or Fat Jar. This is because Uber / Fat jars are not modular
 * and JavaFX checks if the correct modules are present, so using another
 * Launcher acts as a workaround.
 */
public class UberJarLauncher {

    /**
     * Private constructor for the UberJarLauncher.
     * No reason to make an instance of this object, instead use the static
     * method provided.
     * <br>
     * Mainly here to avoid warnings.
     */
    private UberJarLauncher() {

    }

    /**
     * Launch this application.
     *
     * @param args Any additional arguments.
     */
    public static void main(String[] args) {
        Launcher.main(args);
    }

}
