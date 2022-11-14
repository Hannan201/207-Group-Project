package commands;

import views.View;

import java.util.List;

/**
 * This class is responsible for switching the application
 * to light mode.
 */

public class SwitchToLightMode implements Command {

    // List to store all the different views in the application.
    private List<View> views;

    /**
     * Make a command to switch the theme of all the views
     * passed in a list as a parameter to light mode.
     *
     * @param newViews The list of all views.
     */
    public SwitchToLightMode(List<View> newViews) {
        this.views = newViews;
    }

    /**
     * Execute this command to switch the views to
     * light mode.
     */
    @Override
    public void execute() {
        for (View view : this.views) {
            view.switchToLightMode();
        }
    }

}
