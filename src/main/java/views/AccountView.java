package views;

public class AccountView extends View {

    private View firstInstance = null;

    private AccountView() {

    }
    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new AccountView();
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
