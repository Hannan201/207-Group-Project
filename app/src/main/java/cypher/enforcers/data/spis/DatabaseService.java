package cypher.enforcers.data.spis;

/*
 Interface for the database service. This service acts as a way to
 send queries to the database, ensure connection reliability and
 deliver any important alerts.
 */
public interface DatabaseService {

    /**
     * Create a connection to the database by providing the
     * properties.
     *
     * @param name name of the database to connect to.
     */
    void connect(String name);

    /**
     * Connect to the database with the default properties.
     */
    void connect();

    /**
     * Disconnect from the current database.
     */
    void disconnect();

}
