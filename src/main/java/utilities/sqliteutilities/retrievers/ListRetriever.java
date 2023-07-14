package utilities.sqliteutilities.retrievers;

import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * This class is used for when trying to retrieve multiple instances of data
 * from the result set of a SQLite query (such as multiple Strings, Integers, or
 * even custom objects).
 *
 * @param <T> The type of data that will be stored in the list.
 */
public class ListRetriever<T> extends Retriever implements Callback<ResultSet, List<T>> {

    private static final Logger logger = LoggerFactory.getLogger(ListRetriever.class);

    // This callback will be used to retrieve a single value
    // from a column or columns, depending on the implementation.
    private final Callback<ResultSet, T> callback;

    // To store all the resulting values of the SQL query.
    private final List<T> results;

    /**
     * Create a new list retriever.
     *
     * @param results List to store all the results.
     * @param callback Callback function which will holds the implementation
     *                 on how to retrieve just one value from the result set.
     */
    public ListRetriever(List<T> results, Callback<ResultSet, T> callback) {
        super("");
        this.results = results;
        this.callback = callback;
    }

    /**
     * Return a list of all the values from the result set.
     *
     * @param resultSet The result set.
     * @return List of all the values retrieved from result of the
     * SQL query.
     */
    @Override
    public List<T> call(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                results.add(callback.call(resultSet));
            }
        } catch (SQLException e) {
            logger.warn("Failed to populate list with results, aborting request. Cause: ", e);
            e.printStackTrace();
        }

        return results;
    }
}
