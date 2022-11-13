package views;

public class CodeView extends View {

    private View firstInstance = null;

    private CodeView() {

    }

    @Override
    public View getInstance() {
        if (firstInstance == null) {
            firstInstance = new CodeView();
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
