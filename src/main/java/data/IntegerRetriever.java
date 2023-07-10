package data;

import javafx.util.Callback;
import utilities.Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to retrieve a single integer from a column based
 * on the result of a SQLite query.
 */
public class IntegerRetriever extends Retriever implements Callback<ResultSet, Integer> {

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
            e.printStackTrace();
        }

        return -1;
    }
}
