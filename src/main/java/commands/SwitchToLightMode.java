package commands;

import views.View;

import java.util.List;

public class SwitchToLightMode implements Command {

    private List<View> views;

    public SwitchToLightMode(List<View> newViews) {
        this.views = newViews;
    }

    @Override
    public void execute() {
        for (View view : this.views) {
            view.switchToLightMode();
        }
    }

}
