package user;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

/**
 * This class is responsible for loading and
 * saving data for users in this application.
 */

public class Database {

    // To check if a user's already logged in.
    private static boolean loggedIn;

    // To prevent duplicate usernames.
    private static Set<String> usernames;

    // Store current user to load or save data.
    private static User user;

    // Source for the usernames, passwords, and
    // any configurations.
    private static String userSource;

    // To only allow one source to be set
    // for usernames and passwords.
    private static boolean userSourceSet;

    // Source for the social media accounts
    // and backup codes for that account.
    private static String accountsSource;

    // To only allow one source to be
    // set for the backup codes and
    // accounts.
    private static boolean accountSourceSet;

    // This stores the position of each username
    // in the CSV file. To make it easier to load
    // in the other configurations.
    private static Map<String, Long> userRelatedData;

    // To form each row in the CSV file.
    private static String[] userConfigurations = new String[5];

    // To store the location in the file for a specific username.
    private static long filePointer;

    // To store the location in the file for a specific
    // social media account.
    private static long accountsFilePointer;

    /**
     * Sets the source to the CSV file where
     * the usernames and passwords are stored.
     *
     * @param filePath Path to the file.
     */
    public static void setUserSource(String filePath) {
        if (!userSourceSet) {
            usernames = new HashSet<>();
            filePointer = 0;
            userSource = filePath;
            userSourceSet = true;
            userRelatedData = new HashMap<>();
            initializeFilePointers();
        }
    }

    /**
     * Fill the hashmap so that each username is associated
     * to the starting position in the CSV file for where
     * the extra configurations are stored, this is to
     * prevent loading unnecessary data.
     */
    private static void initializeFilePointers() {
        try {
            File file = new File(userSource);
            FileReader in = new FileReader(file);
            BufferedReader readFile = new BufferedReader(in);
            String line;

            // To go through the first two header lines.
            for (int i = 0; i < 2; i++) {
                line = readFile.readLine();
                filePointer += line.getBytes().length;
            }

            // For each row in the CSV file.
            while ((line = readFile.readLine()) != null) {
                String username = line.split(",")[0];
                usernames.add(username);

                // Using the bytes to move the file pointer.
                userRelatedData.put(username, filePointer + username.getBytes().length + 1);
                filePointer += line.getBytes().length;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adjust the file pointer to the correct position
     * for when a new user is added for this application.
     */
    private static void initializeFilePointerForAccounts() {
        // Position in the file for if a new account is added.
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(accountsSource, "r");
            accountsFilePointer = raf.length();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set the source to the file where
     * the social media accounts and
     * backup codes can be found.
     *
     * @param filePath Path to the file.
     */
    public static void setAccountsSource(String filePath) {
        if (!accountSourceSet) {
            accountsSource = filePath;
            accountSourceSet = true;
            initializeFilePointerForAccounts();
        }
    }

    /**
     * Register a new user for this application.
     *
     * @param username Username for the new user.
     * @param password Password for the new user.
     * @return
     */
    public static User registerUser(String username, String password) {
        if (accountSourceSet && userSourceSet && !loggedIn) {
            userConfigurations[0] = username;
            String salt = generateSalt();
            userConfigurations[2] = salt;
            userConfigurations[1] = hashPassword(password, salt.getBytes());
            userConfigurations[3] = "light";
            userConfigurations[4] = Long.toString(accountsFilePointer);
            setLoginStatus("true");
            user = new User(username, password);
            return user;
        }
        return null;
    }

    /**
     * Generate a salt which can be used for hashing algorithms.
     *
     * @return String version of the salt in Base 64
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
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
    private static String hashPassword(String s, byte[] salt) {
        KeySpec spec = new PBEKeySpec(s.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        byte[] hash;
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            return null;
        }

        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Update the current theme for the user.
     *
     * @param newTheme The new theme to update to.
     */
    public static void updateTheme(String newTheme) {
        if (accountSourceSet && userSourceSet && loggedIn) {
            userConfigurations[3] = newTheme;
        }
    }

    /**
     * Logout the user from this application.
     */
    public static void logUserOut() {
        setLoginStatus("false");
        saveUserData();
    }

    public static void saveUserData() {
        if (!usernames.contains(user.getUsername())) {
            File file;
            FileWriter out;
            BufferedWriter writeFile;
            try {
                file = new File(accountsSource);
                out = new FileWriter(file, true);
                writeFile = new BufferedWriter(out);
                writeFile.write(user.getUsername() + "\n");
                writeFile.write(user.getAccounts().size() + "\n");
                for (Account account : user.getAccounts()) {
                    writeFile.write(account.getName() + "\n");
                    writeFile.write(account.getSocialMediaType() + "\n");
                    writeFile.write(account.getUserCodes().size() + "\n");
                    for (String code : account.getUserCodes()) {
                        writeFile.write(code + "\n");
                    }
                }

                writeFile.close();
                out.close();

                file = new File(userSource);
                out = new FileWriter(file, true);
                writeFile = new BufferedWriter(out);

                writeFile.write("\n" + String.join(",", userConfigurations));
                writeFile.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set the status of a user on if they're either logged in
     * or logged out.
     *
     * @param status The new status, either logged in or logged out.
     */
    private static void setLoginStatus(String status) {
        RandomAccessFile raf = null;
        try {
          raf = new RandomAccessFile(userSource, "rw");
          String formatted = String.format("%-5s", status);
          raf.write(formatted.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
