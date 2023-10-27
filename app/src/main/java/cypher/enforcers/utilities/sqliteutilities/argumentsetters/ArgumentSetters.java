package cypher.enforcers.utilities.sqliteutilities.argumentsetters;

import cypher.enforcers.code.Code;
import cypher.enforcers.models.Account;
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

    // How an account should be added to the database.
    private static final BiConsumer<PreparedStatement, Account> FOR_ACCOUNT = (statement, account) -> {
        try {
            statement.setLong(1, account.getUserId());
            statement.setString(2, account.getName());
            statement.setString(3, account.getSocialMediaType());

            ResultSet results = statement.executeQuery();

            account.setId(results.getLong("id"));
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

    // How a code should be added to the database.
    private static final BiConsumer<PreparedStatement, Code> FOR_CODE = (statement, code) -> {
        try {
            statement.setLong(1, code.getAccountID());
            statement.setString(2, code.getCode());

            ResultSet results = statement.executeQuery();

            code.setId(results.getLong("id"));
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
    private static final TriConsumer<PreparedStatement, Integer, Object> FOR_INTEGER = (statement, index, value) -> {
        try {
            statement.setInt(index, (Integer) value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // How a long should be added to the database.
    private static final TriConsumer<PreparedStatement, Integer, Object> FOR_LONG = (statement, index, value) -> {
        try {
            statement.setLong(index, (Long) value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // How a long should be added to the database, if it's the only
    // argument being passed in.
    private static final BiConsumer<PreparedStatement, Long> FOR_ONE_LONG = (statement, value) -> {
        try {
            statement.setLong(1, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // How a string should be added to the database.
    private static final TriConsumer<PreparedStatement, Integer, Object> FOR_STRING = (statement, index, value) -> {
        try {
            statement.setString(index, (String) value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // Maps the type of object to how it should be set for the
    // placeholders.
    private static final Map<Class<?>, BiConsumer<PreparedStatement, ?>> OBJECT_TYPE_TO_SETTER =
            Map.ofEntries(
                    Map.entry(User.class, FOR_USER),
                    Map.entry(Account.class, FOR_ACCOUNT),
                    Map.entry(Long.class, FOR_ONE_LONG),
                    Map.entry(Code.class, FOR_CODE)
            );

    // Maps the type of object to which index it should be added. Since
    // the data being added might not all belong to one class.
    private static final Map<Class<?>, TriConsumer<PreparedStatement, Integer, Object>> SINGLE_TYPE_TO_SETTER =
            Map.ofEntries(
                    Map.entry(Integer.class, FOR_INTEGER),
                    Map.entry(Long.class, FOR_LONG),
                    Map.entry(String.class, FOR_STRING)
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
    public static TriConsumer<PreparedStatement, Integer, Object> getSetter(Class<?> clazz) {
        return SINGLE_TYPE_TO_SETTER.get(clazz);
    }
}