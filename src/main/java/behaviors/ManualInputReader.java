package behaviors;

import behaviors.interfaces.ReadCodeBehavior;

import java.util.List;

/**
 * This class is responsible for getting the backup
 * codes for a specific account through keyboard input.
 */

public class ManualInputReader implements ReadCodeBehavior {

    /**
     * Read backup codes through keyboard input.
     *
     * @return List of backup codes.
     */
    @Override
    public List<String> readCodes() {
        throw new UnsupportedOperationException();
    }
}
