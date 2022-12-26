package controllers;

import javafx.scene.control.Button;
import views.SignInView;
import views.SignUpView;
import views.View;

public class HomePageController {

    public Button signIn;
    public Button signUp;

    /**
     * A handle method for the Sign-In button that opens a pop-up to allow the
     * user to sign-in.
     */
    public void handleSignIn() {
        View.loadNewWindow(SignInView.getInstance());
    }

    /**
     * A handle method for the Sign-Up button that opens a pop-up to allow the
     * user to sign in.
     */
    public void handleSignUp() {
        View.loadNewWindow(SignUpView.getInstance());
    }
}
