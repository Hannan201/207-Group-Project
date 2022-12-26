package behaviors.interfaces;

import java.util.List;

/**
 * An interface to obtain backup codes from the user
 * depending on they'd like to enter them (keyboard input
 * or text file).
 */
public interface ReadCodeBehavior {

    /**
     * Read in backup codes from a source.
     *
     * @return A list of backup codes.
     */
    List<String> readCodes();
}
