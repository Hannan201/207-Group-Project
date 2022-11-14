import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is made with helper functions to generate
 * back up codes.
 */

public class CodeGenerator {

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
