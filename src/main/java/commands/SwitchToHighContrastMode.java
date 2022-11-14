package commands;

import views.View;

import java.util.List;

public class SwitchToHighContrastMode implements Command {

    private List<View> views;

    public SwitchToHighContrastMode(List<View> newViews) {
        this.views = newViews;
    }

    @Override
    public void execute() {
        for (View view : this.views) {
            view.switchToHighContrastMode();
        }
    }
}
