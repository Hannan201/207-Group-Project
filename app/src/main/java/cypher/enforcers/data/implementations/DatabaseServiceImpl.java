package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.DatabaseService;

/*
 Implementation for the database service. This service acts as a way to
 send queries to the database, ensure connection reliability and
 deliver any important alerts.
 */
public class DatabaseServiceImpl implements DatabaseService {

    private static final String DEFAULT_NAME = "database.db";

    /**
     * Create a connection to the database by providing the
     * properties.
     *
     * @param name name of the database to connect to.
     */
    @Override
    public void connect(String name) {

    }

    /**
     * Connect to the database with the default properties.
     */
    @Override
    public void connect() {

    }

    /**
     * Disconnect from the current database.
     */
    @Override
    public void disconnect() {

    }
}
