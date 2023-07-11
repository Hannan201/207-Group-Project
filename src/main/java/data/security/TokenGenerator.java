package data.security;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

/*
 This class is responsible for generating tokens for authentication.

 Credits to: https://stackoverflow.com/a/41156.
 */
public class TokenGenerator {

    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWERCASE = UPPERCASE.toLowerCase(Locale.ROOT);

    public static final String DIGITS = "0123456789";

    public static final String ALPHA_NUMERIC = UPPERCASE + LOWERCASE + DIGITS;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    /**
     * Create identifier.
     *
     * @param length Length of the identifier.
     */
    public TokenGenerator(int length) {
        if (length < 1) throw new IllegalArgumentException();
        this.random = new SecureRandom();
        this.symbols = ALPHA_NUMERIC.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Generate a random string.
     *
     * @return Generated random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

}