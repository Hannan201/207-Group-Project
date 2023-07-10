package data;

import java.sql.PreparedStatement;

/**
 * This class allows to set the argument of a SQLite prepared statement.
 *
 * @param <T> Type of data to be set.
 */
public abstract class ArgumentSetter<T> {

    // Value to be set.
    protected T value;

    /**
     * Create a new argument setter.
     *
     * @param value The value to be set.
     */
    public ArgumentSetter(T value) {
        this.value = value;
    }


    /**
     * Set the argument of a prepared statement at a specific index.
     *
     * @param statement The prepared statement.
     * @param index The index at which the argument needs to be set.
     */
    public abstract void setArgument(PreparedStatement statement, int index);
}
