package cypher.enforcers.utilities.sqliteutilities.argumentsetters;

import cypher.enforcers.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This class is used to define how different types of object should be
 * set when being used as placeholders in an SQL query.
 */
public class ArgumentSetters {

    // How a user should be added to the database.
    private static final BiConsumer<PreparedStatement, User> FOR_USER = (statement, user) -> {
        try {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            statement.execute();

            ResultSet results = statement.getResultSet();

            user.setID(results.getLong("id"));
            statement.close();
        } catch (SQLException e) {
            try {
                statement.close();
            } catch (SQLException onClose) {
                throw new RuntimeException(onClose);
            }

            throw new RuntimeException(e);
        }
    };

    // How an integer should be added to the database.
    private static final TriConsumer<PreparedStatement, Integer, Integer> FOR_INTEGER = (statement, index, value) -> {
        try {
            statement.setInt(index, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // Maps the type of object to how it should be set for the
    // placeholders.
    private static final Map<Class<?>, BiConsumer<PreparedStatement, ?>> OBJECT_TYPE_TO_SETTER =
            Map.ofEntries(
                    Map.entry(User.class, FOR_USER)
            );

    // Maps the type of object to which index it should be added. Since
    // the data being added might not all belong to one class.
    private static final Map<Class<?>, TriConsumer<PreparedStatement, Integer, ?>> SINGLE_TYPE_TO_SETTER =
            Map.ofEntries(
                    Map.entry(Integer.class, FOR_INTEGER)
            );

    /**
     * Get the argument setter when related data in one class is being
     * used for the placeholder.
     *
     * @param clazz The type of object being added.
     * @return A BiConsumer that knows how to add this object for the
     * SQL query.
     */
    public static BiConsumer<PreparedStatement, ?> getObjectSetter(Class<?> clazz) {
        return OBJECT_TYPE_TO_SETTER.get(clazz);
    }

    /**
     * Get the argument setter for that takes in an index to where
     * the object should be added.
     *
     * @param clazz The type of object being added.
     * @return A TriConsumer that knows how to add the argument to the
     * placeholder with the index being specified.
     */
    public static TriConsumer<PreparedStatement, Integer, ?> getSetter(Class<?> clazz) {
        return SINGLE_TYPE_TO_SETTER.get(clazz);
    }
}