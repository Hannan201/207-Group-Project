package cypher.enforcers.data.spis;

import cypher.enforcers.utilities.sqliteutilities.ArgumentSetters;
import cypher.enforcers.utilities.sqliteutilities.TriConsumer;
import cypher.enforcers.utilities.sqliteutilities.Retrievers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 Interface for the database service. This service acts as a way to
 send queries to the database, ensure connection reliability and
 deliver any important alerts.
 */
public interface DatabaseService {

    /**
     * Create a connection to the database by providing the name.
     *
     * @param name name of the database to connect to.
     */
    void connect(String name);

    /**
     * Connect to the database with the default properties.
     */
    void connect();

    /**
     * Get the connection object.
     *
     * @return The connection.
     */
    Connection getConnection();

    /**
     * Check if this database service is not connected.
     *
     * @return True if not connected, false otherwise.
     * @throws SQLException if anything goes wrong while checking
     * for the connection status.
     */
    default boolean noConnection() throws SQLException {
        Connection con = getConnection();
        return Objects.isNull(con) || con.isClosed();
    }

    /**
     * Execute an update query with placeholders for an object.
     *
     * @param query The query to execute.
     * @param object The object data being updated.
     * @param <T> The type of object, this will be used to determine
     *           how to set the object.
     * @throws SQLException If anything goes wrong in the update. In which
     * case, the table will be rolled back.
     */
    @SuppressWarnings("unchecked")
    default <T> void executeUpdate(String query, T object) throws SQLException {
        if (noConnection()) {
            throw new SQLException("No connection.");
        }

        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            getConnection().setAutoCommit(false);
            BiConsumer<PreparedStatement, T> consumer = (BiConsumer<PreparedStatement, T>) ArgumentSetters.getObjectSetter(object.getClass());
            consumer.accept(statement, object);
            statement.executeUpdate();
            getConnection().commit();
        } catch (SQLException e) {
            getConnection().rollback();
        }
    }

    /**
     * Execute an update statement with multiple placeholders.
     *
     * @param query The query.
     * @param objects The values to be used as placeholders.
     * @throws SQLException If anything goes wrong. In which the table
     * will be rolled back.
     */
    default void executeUpdate(String query, Object ... objects) throws SQLException {
        if (noConnection()) {
            throw new SQLException("No connection.");
        }

        if (noMatch(query, objects.length)) {
            throw new SQLException("Number of arguments does not match number of place holders.");
        }

        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            getConnection().setAutoCommit(false);

            for (int i = 0; i < objects.length; i++) {
                TriConsumer<PreparedStatement, Integer, Object> setter
                        = ArgumentSetters.getSetter(objects[i].getClass());
                setter.accept(statement, i + 1, objects[i]);
            }

            statement.executeUpdate();
            getConnection().commit();
        } catch (SQLException e) {
            getConnection().rollback();
        }
    }

    /**
     * Execute a select statement to obtain the result based on the
     * class-type of an object.
     *
     * @param query The query to execute.
     * @param type The class type of the object to retrieve.
     * @param objects The arguments that need to be set for the placeholder.
     * @param <T> The type of value that should be returned.
     * @return The value, if any errors occur, null will be returned.
     * @throws SQLException If anything goes wrong.
     */
    default <T> T executeSelect(String query, Class<T> type, Object ... objects) throws SQLException {
        if (noConnection()) {
            throw new SQLException("No connection.");
        }

        if (noMatch(query, objects.length)) {
            throw new SQLException("Number of arguments does not match number of place holders.");
        }

        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < objects.length; i++) {
                TriConsumer<PreparedStatement, Integer, Object> setter
                        = ArgumentSetters.getSetter(objects[i].getClass());
                setter.accept(statement, i + 1, objects[i]);
            }

            return Retrievers.get(type).apply(statement.executeQuery());
        }
    }

    /**
     * Execute a select statement to obtain multiple results in a list
     * based on the class-type of an object.
     *
     * @param query The query to execute.
     * @param type The class type of the object that should be in the list.
     * @param objects The arguments that need to be set for the placeholder.
     * @param <T> The type of value that should be in the list.
     * @return The list of values, if any errors occur or no results
     * are found null will be returned.
     * @throws SQLException If anything goes wrong.
     */
    default <T> List<T> executeMultiSelect(String query, Class<T> type, Object ... objects) throws SQLException {
        if (noConnection()) {
            throw new SQLException("No connection.");
        }

        if (noMatch(query, objects.length)) {
            throw new SQLException("Number of arguments does not match number of place holders.");
        }

        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < objects.length; i++) {
                TriConsumer<PreparedStatement, Integer, Object> setter
                        = ArgumentSetters.getSetter(objects[i].getClass());
                setter.accept(statement, i + 1, objects[i]);
            }

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            List<T> results = new ArrayList<>();
            Function<ResultSet, T> function = Retrievers.get(type);

            do {
                results.add(function.apply(resultSet));
            } while (resultSet.next());

            return results;
        }
    }

    /**
     * Disconnect from the current database.
     */
    void disconnect();

    /**
     * Checks if the number of placeholders in an SQL query do not
     * the amount of argument passed in.
     *
     * @param query The query.
     * @param amount The number of arguments passed in.
     * @return True if they don't match, false otherwise.
     */
    default boolean noMatch(String query, int amount) {
        return (query.length() - query.replace("?", "").length()) != amount;
    }

}
