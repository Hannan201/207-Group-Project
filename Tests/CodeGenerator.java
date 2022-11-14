import java.util.Random;

public class CodeGenerator {

    private static final String ALPHANUMERIC = "123456789abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "1234567890";

    public static String makeSegment(String text, int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i <= length; i++) {
            int index = random.nextInt(text.length());
            result.append(text.charAt(index));
        }

        return result.toString();
    }

    public static String makeCodeWithDigits(int length, String delimiter) {
        String code = makeSegment(NUMERIC, length);
        code += delimiter;
        code += makeSegment(NUMERIC, length);

        return code;
    }

    public static String makeCodeWithAlphaNumeric(int length, String delimiter) {
        String code = makeSegment(ALPHANUMERIC, length);
        code += delimiter;
        code += makeSegment(ALPHANUMERIC, length);

        return code;
    }
}
