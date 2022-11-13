package views;

public class SignUpView extends View {

    private View firstInstance = null;

    private SignUpView() {

    }

    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SignUpView();
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
