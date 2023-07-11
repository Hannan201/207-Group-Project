package utilities.sqliteutilities.retrievers;

import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to retrieve a single String from a column based
 * on the result of a SQLite query.
 */
public class StringRetriever extends Retriever implements Callback<ResultSet, String> {

    /**
     * Create a new String retriever.
     *
     * @param key Name of column to retrieve the String from.
     */
    public StringRetriever(String key) {
        super(key);
    }

    /**
     * Retrieve the String from the result set.
     *
     * @param resultSet Result set from the SQL query.
     * @return String value found in the column.
     */
    @Override
    public String call(ResultSet resultSet) {
        try {
            return resultSet.getString(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}