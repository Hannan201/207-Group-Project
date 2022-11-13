package views;

public class AddAccountView extends View {

    private View firstInstance = null;

    private AddAccountView() {

    }

    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new AddAccountView();
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
