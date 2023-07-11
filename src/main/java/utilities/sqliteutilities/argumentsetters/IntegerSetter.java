package utilities.sqliteutilities.argumentsetters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class allows to set the argument of a SQLite prepared statement to
 * be an integer.
 */
public class IntegerSetter extends ArgumentSetter<Integer> {

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
            e.printStackTrace();
        }
    }
}
