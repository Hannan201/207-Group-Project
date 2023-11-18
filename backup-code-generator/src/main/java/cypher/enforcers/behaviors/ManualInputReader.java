package cypher.enforcers.behaviors;

import cypher.enforcers.behaviors.interfaces.ReadCodeBehavior;

import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for getting the backup
 * codes for a specific account through keyboard input.
 */
public class ManualInputReader implements ReadCodeBehavior {

    /** Logger for the manual input reader. */
    private static final Logger logger = LoggerFactory.getLogger(ManualInputReader.class);

    /** To store what the user types in. */
    private final TextField code;

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
        logger.debug("Attempting to read from manual input source.");
        return new ArrayList<>(List.of(code.getText()));
    }
}
