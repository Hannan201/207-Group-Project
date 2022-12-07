/*
Class used to generate temporary data.
 */

import user.Account;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TempData {

    public static User user;

    static {
        // Make three accounts
        Account a1 = new Account("Test", "Discord");
        for (int i = 0; i < 16; i++) {
            a1.addCodes(generateCodeAlphaNumeric(4, "-"));
        }

        Account a2 = new Account("Test2", "GitHub");
        for (int i = 0; i < 16; i++) {
            a2.addCodes(generateCodeAlphaNumeric(5, "-"));
        }

        Account a3 = new Account("Test3", "Google");
        for (int i = 0; i < 16; i++) {
            a3.addCodes(generateCodeNumbers(4, " "));
        }

        user = new User("Test");
        user.addNewAccount(a1);
        user.addNewAccount(a2);
        user.addNewAccount(a3);
    }

    // String to choose from numbers and lowercase letters.
    private static final String ALPHANUMERIC = "123456789abcdefghijklmnopqrstuvwxyz";

    // String to choose from just numbers.
    private static final String NUMERIC = "1234567890";

    /**
     * Return a string of length based on the parameter
     * where each character in the string will be from
     * the string text passed in as a parameter.
     *
     * @param text The string to choose the random
     *             characters from.
     * @param length The size of the string.
     * @return The string contain the random characters
     * from text of length based on the parameters.
     */
    public static String makeSegment(String text, int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(text.length());
            result.append(text.charAt(index));
        }

        return result.toString();
    }

    /**
     * Generate a specific amount of backup codes
     * containing only digits in the form XXX-XXX
     * where the number of X's on each side is
     * determined by the length parameter, and the
     * symbol between those X's is determined by the
     * delimiter parameter.
     *
     * For example:
     * generateCodesNumbers(1, 4, " ") could give ["1123 4546"].
     * generateCodesNumbers(1, 4, "-") could give ["1123-4546"].
     *
     * @param count The amount of codes to generate.
     * @param length The amount of numbers on each side.
     * @param delimiter The symbol to split the codes.
     * @return Codes generated in a list.
     */
    public static List<String> generateCodesNumbers(int count, int length, String delimiter) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(generateCodeNumbers(length, delimiter));
        }
        return list;
    }

    /**
     * Generate a specific amount of backup codes
     * containing alphanumeric characters in the form
     * XXX-XXX where the number of X's on each side is
     * determined by the length parameter, and the
     * symbol between those X's is determined by the
     * delimiter parameter.
     *
     * For example:
     * generateCodesNumbers(1, 4, " ") could give ["1a1a 45fh"].
     * generateCodesNumbers(1, 4, "-") could give ["bc12-456a"].
     *
     * @param count The amount of codes to generate.
     * @param length The amount of characters on each side.
     * @param delimiter The symbol split the codes.
     * @return Codes generated in a list.
     */
    public static List<String> generateCodesAlphanumeric(int count, int length, String delimiter) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(generateCodeAlphaNumeric(length, delimiter));
        }
        return list;
    }

    /**
     * Generate a backup code containing only digits
     * in the form XXX-XXX where the number of X's
     * on each side is determined by the length
     * parameter, and the symbol between those X's
     * is determined by the delimiter parameter.
     *
     * @param length Length of each side of the code.
     * @param delimiter The symbol to split each side
     *                  of the code.
     * @return Generated code containing only digits.
     */
    public static String generateCodeNumbers(int length, String delimiter) {
        return makeSegment(NUMERIC, length) + delimiter + makeSegment(NUMERIC, length);
    }

    /**
     * Generate a backup code containing only
     * alphanumeric characters in the form XXX-XXX
     * where the number of X's on each side is
     * determined by the length parameter, and
     * the symbol between those X's is determined
     * by the delimiter parameter.
     *
     * @param length Length of each side of the code.
     * @param delimiter The symbol to place in the
     *                  centre of the code.
     * @return Generated code containing alphanumeric
     * characters.
     */
    public static String generateCodeAlphaNumeric(int length, String delimiter) {
        return makeSegment(ALPHANUMERIC, length) + delimiter + makeSegment(ALPHANUMERIC, length);
    }
}
