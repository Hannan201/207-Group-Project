package commands;

import views.View;

import java.util.List;

public class SwitchToDarkMode implements Command {

    private List<View> views;

    public SwitchToDarkMode(List<View> newViews) {
        this.views = newViews;
    }

    @Override
    public void execute() {
        for (View view : this.views) {
            view.switchToDarkMode();
        }
    }
}
