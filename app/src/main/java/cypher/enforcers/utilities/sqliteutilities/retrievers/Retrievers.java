package cypher.enforcers.utilities.sqliteutilities.retrievers;

import java.sql.ResultSet;
import java.sql.SQLException;
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

}
