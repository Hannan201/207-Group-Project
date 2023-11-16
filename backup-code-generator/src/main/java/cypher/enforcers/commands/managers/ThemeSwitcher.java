package cypher.enforcers.commands.managers;

import cypher.enforcers.commands.Command;

/**
 * This class is responsible for switching the theme
 * of the application.
 */
public class ThemeSwitcher {

    /** The command to change the theme. */
    private Command command;

    /**
     * Creates an object to switch the theme of the
     * application with the command to a specific theme
     * passed in as a parameter.
     *
     * @param newCommand The command to that switches the
     *                   theme.
     */
    public ThemeSwitcher(Command newCommand) {
        this.command = newCommand;
    }

    /**
     * Switch the theme for this application.
     *
     * @throws NullPointerException If there's any missing data that
     * prevents the theme from being changed, such as icons.
     */
    public void switchTheme() throws NullPointerException {
        command.execute();
    }

    /**
     * Change the command for this theme switcher, in
     * case the application needs to be switched to a
     * new theme.
     *
     * @param newCommand The command to change to the
     *                   new theme.
     */
    public void setCommand(Command newCommand) {
        this.command = newCommand;
    }
}
