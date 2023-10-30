package cypher.enforcers.utilities.sqliteutilities.retrievers;

import javafx.util.Callback;
import cypher.enforcers.code.CodeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to retrieve a single Code object based on the
 * result of a SQLite query.
 */
public class CodeRetriever extends Retriever implements Callback<ResultSet, CodeEntity> {

    // Logger for the code retriever.
    private static final Logger logger = LoggerFactory.getLogger(CodeRetriever.class);

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
    public CodeEntity call(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String code = resultSet.getString("code");
            return new CodeEntity(id, code);
        } catch (SQLException e) {
            logger.warn("Failed to retrieve Code, returning null. Cause: ", e);
        }

        return null;
    }
}
