package cypher.enforcers.data.implementations;

import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.code.Code;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.CodeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/*
 Implementation for the Code Data Access Object (DAO) to communicate to the
 database and make changes to any information related to the accounts.
 */
public class CodeDAOImpl implements CodeDAO {

    private static final String ADD_CODE = "INSERT INTO codes (account_id, code) VALUES (?, ?) RETURNING id";

    private static final String DELETE_CODE = "DELETE FROM codes WHERE id = ?";

    private static final String DELETE_CODES = "DELETE FROM codes WHERE account_id = ?";

    // Logger for the code data access object.
    private static final Logger logger = LoggerFactory.getLogger(CodeDAOImpl.class);

    // Service to communicate to the database.
    @SimpleService
    private DatabaseService databaseService;

    /**
     * Get all codes for an account.
     *
     * @param accountID The ID of the account.
     * @return List of codes. Returns an empty list if no codes
     * are found.
     */
    @Override
    public List<Code> getCodes(long accountID) {
        System.out.println("Getting codes for user");
        return null;
    }

    /**
     * Get a code by ID.
     *
     * @param codeID ID of the code to retrieve.
     * @return An optional containing the code if found. Null otherwise.
     */
    @Override
    public Optional<Code> getCode(long codeID) {
        System.out.println("Getting code for user");
        return Optional.empty();
    }

    /**
     * Add a code.
     *
     * @param code The code to add.
     * @return True if the code was added, false otherwise.
     */
    @Override
    public boolean addCode(Code code) {
        try {
            databaseService.executeUpdate(ADD_CODE, code);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return false;
        }

        return true;
    }

    /**
     * Remove a code.
     *
     * @param codeID ID of the code.
     * @return True if code was removed successfully, false otherwise.
     */
    @Override
    public boolean removeCode(long codeID) {
        try {
            databaseService.executeUpdate(DELETE_CODE, codeID);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return false;
        }

        return true;
    }

    /**
     * Remove all codes for an account.
     *
     * @param accountID ID of the account.
     * @return True if the codes was removed successfully,
     * false otherwise.
     */
    @Override
    public boolean clearAllCodes(long accountID) {
        try {
            databaseService.executeUpdate(DELETE_CODES, accountID);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return false;
        }

        return true;
    }
}
