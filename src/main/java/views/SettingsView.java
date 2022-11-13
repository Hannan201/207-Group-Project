package views;

public class SettingsView extends View {

    private View firstInstance = null;

    private SettingsView() {

    }

    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SettingsView();
        }

        return firstInstance;
    }

    @Override
    protected void initUI() {

    }

    @Override
    public void switchToLightMode() {

    }

    @Override
    public void switchToDarkMode() {

    }

    @Override
    public void switchToHighContrastMode() {

    }
}
