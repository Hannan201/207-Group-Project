package cypher.enforcers.utilities.sqliteutilities.retrievers;

import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.utilities.Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to retrieve a single integer from a column based
 * on the result of a SQLite query.
 */
public class IntegerRetriever extends Retriever implements Callback<ResultSet, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(IntegerRetriever.class);

    /**
     * Create a new integer retriever.
     *
     * @param key Name of column to retrieve the integer from.
     */
    public IntegerRetriever(String key) {
        super(key);
    }

    /**
     * Retrieve the integer from the result set.
     * If they key is a string representation of a positive integer it will
     * be converted to an integer and used as the column number to retrieve
     * the data.
     *
     * @param resultSet Result set from the SQL query.
     * @return Integer value found in the column.
     */
    @Override
    public Integer call(ResultSet resultSet) {
        try {
            if (Utilities.isInteger(key)) {
                return resultSet.getInt(Integer.parseInt(key));
            }

            return resultSet.getInt(key);
        } catch (SQLException e) {
            if (Utilities.isInteger(key)) {
                logger.warn(String.format("Failed to retrieve column %s, returning -1. Cause: ", key), e);
            } else {
                logger.warn(String.format("Failed to retrieve key %s, returning -1. Cause: ", key), e);
            }
            e.printStackTrace();
        }

        return -1;
    }
}
