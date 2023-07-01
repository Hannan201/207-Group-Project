package data;

import data.security.Token;

/**
 * This class acts as a global storage so the current loaded token can
 * be accessed from anywhere. This is because this application can only
 * have one user logged in at a time.
 * <p>
 * Similar to Session Storage from JavaScript.
 */
public class Storage {

    // The current token. Null means that no user is
    // currently logged in.
    private static Token token = null;

    /**
     * Update the current token.
     *
     * @param newToken The new token.
     */
    public static void setToken(Token newToken) {
        token = newToken;
    }

    /**
     * Get the current token.
     *
     * @return The token.
     */
    public static Token getToken() {
        return token;
    }
}
