package behaviors;

import behaviors.interfaces.ReadCodeBehavior;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for getting the backup
 * codes for a specific account through keyboard input.
 */

public class ManualInputReader implements ReadCodeBehavior {

    // To store what the user types in.
    private TextField code;

    /**
     * Make a new manual input reader which is linked via the
     * keyboard to detect what the user types in.
     *
     * @param source The source that will store what the user
     *               types in.
     */
    public ManualInputReader(TextField source) {
        this.code = source;
    }

    /**
     * Read backup codes through keyboard input.
     *
     * @return List of backup codes.
     */
    @Override
    public List<String> readCodes() {
        return new ArrayList<>(List.of(code.getText()));
    }
}
