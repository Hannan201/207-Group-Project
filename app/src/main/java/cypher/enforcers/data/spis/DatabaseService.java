package cypher.enforcers.data.spis;

import cypher.enforcers.utilities.sqliteutilities.argumentsetters.ArgumentSetters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

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
     * Execute an update query with placeholders for an object. The corresponding
     * ArgumentSetter implementation will be used to set the placeholder
     * values.
     *
     * @param query The query to execute.
     * @param object The object data being updated.
     * @param <T> The type of object, this will be used to determine
     *           how to set the object.
     * @throws SQLException If anything goes wrong in the update. In which
     * case the table will be rolled back.
     */
    @SuppressWarnings("unchecked")
    default  <T> void executeUpdate(String query, T object) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            getConnection().setAutoCommit(false);
            BiConsumer<PreparedStatement, T> consumer = (BiConsumer<PreparedStatement, T>) ArgumentSetters.getObjectSetter(object.getClass());
            consumer.accept(statement, object);

            if (!statement.isClosed()) {
                statement.executeUpdate();
            }

            getConnection().commit();
        } catch (SQLException e)  {
            getConnection().rollback();
        }
    }

    /**
     * Disconnect from the current database.
     */
    void disconnect();

}
