package data;

public class Storage {

    private static Token token = null;

    public static void setToken(Token newToken) {
        token = newToken;
    }

    public static Token getToken() {
        return token;
    }
}
