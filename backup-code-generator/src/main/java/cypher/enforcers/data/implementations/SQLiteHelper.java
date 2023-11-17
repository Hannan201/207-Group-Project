package cypher.enforcers.data.implementations;

import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.utilities.Utilities;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.*;

/**
 Implementation for the database service. This service acts as a way to
 send queries to the database, ensure connection reliability and
 deliver any important alerts.
 */
public class SQLiteHelper implements DatabaseService {

    /** Logger for the SQLite helper. */
    private static final Logger logger = LoggerFactory.getLogger(SQLiteHelper.class);

    /** Default name of the database. */
    private static final String DEFAULT_NAME = "database/database.db";

    /** Connection to the database. */
    private Connection connection;

    /**
     * Create a new SQLite helper to interact with an SQLite database.
     * <br>
     * Mainly here to avoid warnings.
     */
    public SQLiteHelper() {

    }

    /**
     * Create a connection to the database by providing the name.
     *
     * @param name name of the database to connect to.
     */
    @Override
    public void connect(String name) {
        try {
            Utilities.copyResourceFileIf(name);

            SQLiteConfig configurations = new SQLiteConfig();
            configurations.enforceForeignKeys(true);
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" +
                            Utilities.getParentDirectory() +
                            File.separator +
                            FilenameUtils.getName(name),
                    configurations.toProperties()
            );
        } catch (SQLException | NullPointerException e) {
            logger.warn("Connection to database failed. No new changes will be saved. Cause: ", e);
            return;
        }

        logger.info("Database connected.");
    }

    /**
     * Connect to the database with the default properties.
     */
    @Override
    public void connect() {
        connect(DEFAULT_NAME);
    }

    /**
     * Get the connection object.
     *
     * @return The connection.
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Disconnect from the current database.
     */
    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Failed to close connection to database. Cause: ", e);
            return;
        }

        logger.info("Closed connection to database.");
    }
}
