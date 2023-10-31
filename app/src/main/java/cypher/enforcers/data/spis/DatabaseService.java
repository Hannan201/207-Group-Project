package cypher.enforcers.data.spis;

import cypher.enforcers.utilities.sqliteutilities.argumentsetters.ArgumentSetters;
import cypher.enforcers.utilities.sqliteutilities.argumentsetters.TriConsumer;
import cypher.enforcers.utilities.sqliteutilities.retrievers.Retrievers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/*
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
     * Execute an update query with placeholders for an object.
     *
     * @param query The query to execute.
     * @param object The object data being updated.
     * @param <T> The type of object, this will be used to determine
     *           how to set the object.
     * @throws SQLException If anything goes wrong in the update. In which
     * case the table will be rolled back.
     */
    @SuppressWarnings("unchecked")
    default <T> void executeUpdate(String query, T object) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            getConnection().setAutoCommit(false);
            BiConsumer<PreparedStatement, T> consumer = (BiConsumer<PreparedStatement, T>) ArgumentSetters.getObjectSetter(object.getClass());
            consumer.accept(statement, object);

            if (!statement.isClosed()) {
                statement.executeUpdate();
            }

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
     * Execute a select statement and obtain the result based on
     * a custom implementation on how to retrieve the object, with
     * the name of the key(s) provided.
     *
     * @param query The query to execute.
     * @param retriever Retriever which has the implementation of how
     *                  the data should be obtained from the result set.
     * @param objects The arguments that need to be set for the placeholder.
     * @param <T> The socialMediaType of value that should be returned.
     * @return The value, if any errors occur null will be returned.
     * @throws SQLException If anything goes wrong.
     */
    default <T> T executeSelect(String query, Function<ResultSet, T> retriever, Object ... objects) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < objects.length; i++) {
                TriConsumer<PreparedStatement, Integer, Object> setter
                        = ArgumentSetters.getSetter(objects[i].getClass());
                setter.accept(statement, i + 1, objects[i]);
            }
            return retriever.apply(statement.executeQuery());
        }
    }

    /**
     * Execute a select statement to obtain the result based on the
     * class-socialMediaType of an object.
     *
     * @param query The query to execute.
     * @param type The class socialMediaType of the object to retrieve.
     * @param objects The arguments that need to be set for the placeholder.
     * @param <T> The socialMediaType of value that should be returned.
     * @return The value, if any errors occur null will be returned.
     * @throws SQLException If anything goes wrong.
     */
    default <T> T executeSelect(String query, Class<T> type, Object ... objects) throws SQLException {
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
     * based on the class-socialMediaType of an object.
     *
     * @param query The query to execute.
     * @param type The class socialMediaType of the object that should be in the list.
     * @param objects The arguments that need to be set for the placeholder.
     * @param <T> The socialMediaType of value that should be in the list.
     * @return The list of values, if any errors occur null will be returned.
     * @throws SQLException If anything goes wrong.
     */
    default <T> List<T> executeMultiSelect(String query, Class<T> type, Object ... objects) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < objects.length; i++) {
                TriConsumer<PreparedStatement, Integer, Object> setter
                        = ArgumentSetters.getSetter(objects[i].getClass());
                setter.accept(statement, i + 1, objects[i]);
            }

            List<T> results = new ArrayList<>();
            Function<ResultSet, T> function = Retrievers.get(type);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                results.add(function.apply(resultSet));
            }

            return results;
        }
    }

    /**
     * Disconnect from the current database.
     */
    void disconnect();

}
