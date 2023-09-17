package cypher.enforcers.utilities.sqliteutilities.argumentsetters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class allows to set the argument of a SQLite prepared statement to
 * be an integer.
 */
public class IntegerSetter extends ArgumentSetter<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(IntegerSetter.class);

    /**
     * Create a new integer setter.
     *
     * @param value The integer value to be set.
     */
    public IntegerSetter(Integer value) {
        super(value);
    }

    /**
     * Set the argument of a prepared statement at a specific index to
     * be an integer.
     *
     * @param statement The prepared statement.
     * @param index The index at which the argument needs to be set.
     */
    @Override
    public void setArgument(PreparedStatement statement, int index) {
        try {
            statement.setInt(index, value);
        } catch (SQLException e) {
            logger.warn(String.format("Failed to set argument %d at index %d. Cause: ", value, index), e);
            e.printStackTrace();
        }
    }
}
