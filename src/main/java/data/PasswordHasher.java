package data;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/*
This class is responsible for hashing passwords
for users in this application.
 */
public class PasswordHasher {

    // Algorithm being used to hash passwords.
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    // Number of iterations to apply for this algorithm.
    private static final int NUMBER_OF_ITERATIONS = 1000;

    // Length of the key being used to hash passwords.
    private static final int KEY_LENGTH = 128;

    // Object to generate a new salt.
    private SecureRandom saltGenerator;

    // Object to get the object which gives the object to apply
    // the algorithm.
    private SecretKeyFactory algorithmGenerator;

    public PasswordHasher() {
        this.saltGenerator = new SecureRandom();
        try {
            this.algorithmGenerator = SecretKeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a salt which can be used for hashing algorithms.
     *
     * @return String version of the salt in Base 64
     */
    public String generateSalt() {
        byte[] salt = new byte[16];
        this.saltGenerator.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash a given password so that its secure if the file
     * is compromised. For extra security purposes.
     *
     * @param s The password
     * @param salt The salt being used to hash the password
     * @return String version of the hashed password in Base 64
     */
    public String hashPassword(String s, byte[] salt) {
        // Object to make the key needed for the hashing algorithm.
        KeySpec key = new PBEKeySpec(s.toCharArray(), salt, NUMBER_OF_ITERATIONS, KEY_LENGTH);

        byte[] hash;
        try {
            hash = algorithmGenerator.generateSecret(key).getEncoded();
        } catch (InvalidKeySpecException e) {
            return null;
        }

        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verify a password after applying the hashing algorithm
     * on the salt and the password the user types in.
     *
     * @param expectedPassword The expected password after applying
     *                         the algorithm.
     * @param password The password which the user enters.
     * @param salt The salt used for the original password which
     *             is saved in locally.
     * @return True if the passwords match, false otherwise.
     */
    public boolean verifyPassword(String expectedPassword, String password, String salt) {
        String result = hashPassword(password, salt.getBytes());
        if (result == null) {
            return false;
        }

        return result.equals(expectedPassword);
    }
}