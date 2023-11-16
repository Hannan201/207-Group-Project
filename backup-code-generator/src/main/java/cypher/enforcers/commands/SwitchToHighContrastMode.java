package cypher.enforcers.commands;

import cypher.enforcers.views.View;

import java.util.List;

/**
 * This class is responsible for switching the application
 * to high contrast mode.
 */
public class SwitchToHighContrastMode implements Command {

    /** List to store all the different views in the application. */
    private final List<View> views;

    /**
     * Make a command to switch the theme of all the views
     * passed in a list as a parameter to high contrast mode.
     *
     * @param newViews The list of all views.
     */
    public SwitchToHighContrastMode(List<View> newViews) {
        this.views = newViews;
    }

    /**
     * Execute this command to switch the views to
     * high contrast mode.
     *
     * @throws NullPointerException If there's any missing data for
     * switching the theme to high contrast mode (such as icons).
     */
    @Override
    public void execute() throws NullPointerException {
        for (View view : this.views) {
            view.switchToHighContrastMode();
        }
    }
}
