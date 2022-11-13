package views;

public abstract class View {

    public abstract View getInstance();

    protected abstract void initUI();

    public abstract void switchToLightMode();

    public abstract void switchToDarkMode();

    public abstract void switchToHighContrastMode();
}
