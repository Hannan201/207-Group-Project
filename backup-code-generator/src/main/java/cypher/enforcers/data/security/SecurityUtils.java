package cypher.enforcers.data.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * This class is responsible for ensuring passwords are stored in
 * a safe format.
 */
public class SecurityUtils {

    /**
     * Private constructor to create new security utilities.
     * No reason to make an instance of this object, instead use the static
     * methods provided.
     * <br>
     * Mainly here to avoid warnings.
     */
    private SecurityUtils() {

    }

    /** Logger for the security utilities. */
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    /** Algorithm being used to hash passwords. */
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    /** Number of iterations to apply for this algorithm. */
    private static final int NUMBER_OF_ITERATIONS = 1000;

    /** Length of the key being used to hash passwords. */
    private static final int KEY_LENGTH = 128;

    /** Length of the salt. */
    public static final int SALT_LENGTH = 24;

    /** Object to generate a new salt. */
    private static final SecureRandom saltGenerator = new SecureRandom();

    /** Factory to generate secure keys for a specific algorithm. */
    private static SecretKeyFactory keyFactory;

    /**
     * Create the SecretKeyFactory if it does not already exist.
     *
     * @return True if the factory already exists, false otherwise.
     */
    private static boolean createFactory() {
        try {
            keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            return true;
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Unable to create SecretKeyFactory. Cause: ", e);
            keyFactory = null;
            return false;
        }
    }

    /**
     * Generate a salt which can be used for hashing algorithms.
     *
     * @return String version of the salt in Base 64
     */
    public static String createSalt() {
        byte[] salt = new byte[16];
        saltGenerator.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash a given password so that its secure if the file
     * is compromised. For extra security purposes.
     *
     * @param s The password
     * @param salt The salt being used to hash the password
     * @return String version of the hashed password in Base 64 if
     * successfully hashed, otherwise null.
     */
    public static String hashPassword(String s, byte[] salt) {
        if (Objects.isNull(keyFactory) && !createFactory()) {
            return null;
        }

        if (salt.length < 16) {
            return null;
        }

        // Object to make the key needed for the hashing algorithm.
        KeySpec key = new PBEKeySpec(s.toCharArray(), salt, NUMBER_OF_ITERATIONS, KEY_LENGTH);

        byte[] hash;
        try {
            hash = keyFactory.generateSecret(key).getEncoded();
        } catch (InvalidKeySpecException e) {
            logger.warn("Failed to hash password, returning null. Cause: ", e);
            return null;
        }

        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verify a password after applying the hashing algorithm
     * with the salt based on the password the user types in.
     *
     * @param expectedPassword The expected password after applying
     *                         the algorithm.
     * @param password The password which the user enters.
     * @param salt The salt used for the original password which
     *             is saved in locally.
     * @return True if the passwords match, false otherwise.
     */
    public static boolean verifyPassword(String expectedPassword, String password, String salt) {
        String result = hashPassword(password, salt.getBytes());
        if (result == null) {
            return false;
        }

        return result.equals(expectedPassword);
    }
}
