package views;

public class HomePageView extends View {

    private View firstInstance = null;

    private HomePageView() {

    }

    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new HomePageView();
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
