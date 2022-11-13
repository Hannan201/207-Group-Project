package views;

public class SignInView extends View {

    private View firstInstance = null;

    private SignInView() {

    }

    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new SignInView();
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
