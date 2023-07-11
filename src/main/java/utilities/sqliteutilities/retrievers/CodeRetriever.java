package utilities.sqliteutilities.retrievers;

import javafx.util.Callback;
import code.readers.Code;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to retrieve a single Code object based on the
 * result of a SQLite query.
 */
public class CodeRetriever extends Retriever implements Callback<ResultSet, Code> {

    /**
     * Create a new code retriever.
     *
     * @param key Name of column to retrieve the code from.
     */
    public CodeRetriever(String key) {
        super(key);
    }

    /**
     * Retrieve the code from the result set.
     *
     * @param resultSet Result set from the SQL query.
     * @return Code value found in the columns.
     */
    @Override
    public Code call(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String code = resultSet.getString("code");
            return new Code(id, code);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
