package cypher.enforcers.utilities.sqliteutilities.retrievers;

import cypher.enforcers.models.User;
import cypher.enforcers.views.themes.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;

/**
 * This class is used to define how different types of object should be
 * retrieved from a result set after executing a SELECT SQL statement.
 */
public class Retrievers {

    /**
     * A retriever that returns a boolean value based on the column name.
     *
     * @param key The name of the column.
     * @return The boolean value.
     */
    public static Function<ResultSet, Boolean> ofBoolean(String key) {
        return resultSet -> {
            try {
                return resultSet.getBoolean(key);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    // How to retrieve a user from a result set.
    private static final Function<ResultSet, User> FOR_USER = resultSet -> {
        User user;
        try {
            if (!resultSet.isBeforeFirst()) {
                return null;
            }

            user = new User();
            user.setID(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setTheme(Theme.values()[resultSet.getInt("theme_value")]);
            boolean result = resultSet.getShort("logged_in") == 1;
            user.setLoggedIn(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    };

    // Maps the type of object to how it should be retrieved.
    private static final Map<Class<?>, Function<ResultSet, ?>> OBJECT_TYPE_TO_RETRIEVER =
            Map.ofEntries(
                    Map.entry(User.class, FOR_USER)
            );

    /**
     * Get a retriever that can retrieve a specific type of object from
     * a result set.
     *
     * @param type The class of the object to retrieve.
     * @param <T> The type of data to retrieve.
     * @return A retriever that knows how to retrieve the object from
     * the result set.
     */
    @SuppressWarnings("unchecked")
    public static <T> Function<ResultSet, T> get(Class<T> type) {
        return (Function<ResultSet, T>) OBJECT_TYPE_TO_RETRIEVER.get(type);
    }
}