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

    private static long oldPosition;
    private static long newPosition;

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
            filePointer = 0;
            File file = new File(userSource);
            FileReader in = new FileReader(file);
            BufferedReader readFile = new BufferedReader(in);
            String line;

            line = readFile.readLine();

            if (line.strip().equals("true .")) {
                loggedIn = true;
            }

            filePointer += line.getBytes(StandardCharsets.UTF_8).length + 1;

            line = readFile.readLine();
            filePointer += line.getBytes(StandardCharsets.UTF_8).length + 1;

            // For each row in the CSV file.
            while ((line = readFile.readLine()) != null) {
                String username = line.split(",")[0].toLowerCase();
                usernames.add(username);

                // Using the bytes to move the file pointer.
                userRelatedData.put(username, filePointer + username.getBytes(StandardCharsets.UTF_8).length + 1);
                filePointer += line.getBytes(StandardCharsets.UTF_8).length + 1;
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
        accountsFilePointer = 0;
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
     */
    public static void registerUser(String username, String password) {
        if (accountSourceSet && userSourceSet && !loggedIn) {
            userConfigurations[0] = username;
            String salt = generateSalt();
            userConfigurations[2] = salt;
            userConfigurations[1] = hashPassword(password, salt.getBytes());
            userConfigurations[3] = String.format("%-13s", "Light");
            userConfigurations[4] = Long.toString(accountsFilePointer);
            setLoginStatus("true");
            loggedIn = true;
            user = new User(username);
        }
    }

    /**
     * Check whether a user is logged-in to this application.
     *
     * @return True if logged in, false otherwise.
     */
    public static boolean getLoginStatus() {
        return loggedIn;
    }

    /**
     * Check if a username is already taken.
     *
     * @param username The username to check for.
     * @return True if username is taken, false otherwise.s
     */
    public static boolean checkUsername(String username) {
        return usernames.contains(username.toLowerCase());
    }

    public static boolean authenticateUser(String username, String password) {
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(userSource, "r");
            raf.seek(userRelatedData.get(username.toLowerCase()));
            String utf8 = raf.readLine();
            String[] row = utf8.split(",");
            String result = hashPassword(password, row[1].getBytes());
            loggedIn = row[0].equals(result);

            if (loggedIn) {
                oldPosition = Long.parseLong(row[3]);
                loadUserData(oldPosition);
            }
        } catch (Exception e) {
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

        return loggedIn;
    }

    /**
     * Get the current logged-in user object.
     *
     * @return Current logged in user.
     */
    public static User getUser() {
        if (loggedIn) {
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

    /**
     * Save the username, password, current theme,
     * social media accounts, and backup codes
     * for the current logged-in user.
     */
    public static void saveUserData() {
        if (!(loggedIn && userSourceSet && accountSourceSet)) {
            return;
        }

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

                initializeFilePointers();
                initializeFilePointerForAccounts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadUserData(long offset) {
        RandomAccessFile raf = null;
        FileReader in;
        BufferedReader readFile;
        try {
            raf = new RandomAccessFile(accountsSource, "r");
            raf.seek(offset);
            in = new FileReader(raf.getFD());
            readFile = new BufferedReader(in);
            user = new User(readFile.readLine());

            long numOfAccounts = Long.parseLong(readFile.readLine());

            for (int i = 0; i < numOfAccounts; i++) {
                String name = readFile.readLine();
                String platform = readFile.readLine();
                Account account = new Account(name, platform);
                long numOfCodes = Long.parseLong(readFile.readLine());
                for (int j = 0; j < numOfCodes; j++) {
                    account.addCodes(readFile.readLine());
                }
                user.addNewAccount(account);
            }

        } catch (Exception e) {
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
