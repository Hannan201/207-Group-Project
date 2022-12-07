package code.readers;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.*;
import java.io.*;

/**
 * This class is responsible for reading backup codes
 * in the structure provided by Shopify.
 */

public class ShopifyReader extends CodeReader implements ReadCodeBehavior {

    /**
     * Extract the backup codes from a text file in the
     * structure given by Shopify.
     *
     * @param fileName Location to the text file.
     * @return The list of backup codes in the file.
     */
    @Override
    public List<String> extractCodes(String fileName) {
        List<String> codes = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner shopifyReader = new Scanner(file);

            // Skip past the first 8 lines of the txt file. This should make the next line to be read a line with a code
            for (int i = 0; i < 8; i++) {
                shopifyReader.nextLine();
            }

            // Read each code in the file and add it to the list
            while (shopifyReader.hasNextLine()) {
                String currCode = shopifyReader.nextLine().strip();
                if (!currCode.equals("")){
                    codes.add(currCode);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }

    /**
     * Read the backup codes from a text file in the
     * structure provided by Shopify.
     *
     * @return The list of backup codes.
     */
    @Override
    public List<String> readCodes() {
        return extractCodes(this.getFilePath());
    }
}
