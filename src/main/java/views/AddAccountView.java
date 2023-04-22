package views;

/**
 * This class is responsible for displaying a view
 * to add a new social media account.
 */

public class AddAccountView extends View {

    // An instance for this add-new-account view.
    private static View firstInstance = null;

    /**
     * Create a new add-new-account view.
     */
    private AddAccountView() {
        initUI();
    }

    /**
     * Return the instance of this add-new-account view.
     *
     * @return Instance of this add-new-account view.
     */
    public static View getInstance() {
        if (firstInstance == null) {
            firstInstance = new AddAccountView();
        }

        return firstInstance;
    }

    /**
     * Initialise the UI elements for this add-new-account
     * view.
     */
    @Override
    protected void initUI() {
        this.names = new String[]{"CreateAccountView.css",
                                  "CreateAccountViewDM.css",
                                  "CreateAccountViewHC.css"};

        this.loadStylesheets();

        this.loadRoot("CreateAccountView.fxml");
    }
}
