package cypher.enforcers.utilities.sqliteutilities.argumentsetters;

import cypher.enforcers.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ArgumentSetters {

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

    private static final TriConsumer<PreparedStatement, Integer, Integer> FOR_INTEGER = (statement, index, value) -> {
        try {
            statement.setInt(index, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    private static final Map<Class<?>, BiConsumer<PreparedStatement, ?>> OBJECT_TYPE_TO_SETTER =
            Map.ofEntries(
                    Map.entry(User.class, FOR_USER)
            );

    private static final Map<Class<?>, TriConsumer<PreparedStatement, Integer, ?>> SINGLE_TYPE_TO_SETTER =
            Map.ofEntries(
                    Map.entry(Integer.class, FOR_INTEGER)
            );

    public static BiConsumer<PreparedStatement, ?> getObjectSetter(Class<?> clazz) {
        return OBJECT_TYPE_TO_SETTER.get(clazz);
    }

    public static TriConsumer<PreparedStatement, Integer, ?> getSetter(Class<?> clazz) {
        return SINGLE_TYPE_TO_SETTER.get(clazz);
    }


}
