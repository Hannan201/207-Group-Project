package cypher.enforcers.data.implementations;

import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.code.Code;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.CodeDAO;
import cypher.enforcers.models.Account;
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

    private static final String UPDATE_CODE = "UPDATE codes SET code = ? WHERE id = ?";

    private static final String GET_CODES = "SELECT * FROM codes WHERE account_id = ?";

    private static final String GET_CODE = "SELECT * FROM codes WHERE id = ?";

    // Logger for the code data access object.
    private static final Logger logger = LoggerFactory.getLogger(CodeDAOImpl.class);

    // Service to communicate to the database.
    @SimpleService
    private DatabaseService databaseService;

    /**
     * Get all codes for an account.
     *
     * @param account The account to retrieve the codes for.
     * @return List of codes. Returns null if any errors occur.
     */
    @Override
    public List<Code> getCodes(Account account) {
        try {
            return databaseService.executeMultiSelect(GET_CODES, Code.class, account.getID());
        } catch (SQLException e) {
            logger.debug("Failed select query. Cause: ", e);
        }

        return null;
    }

    /**
     * Get a code by ID.
     *
     * @param codeID ID of the code to retrieve.
     * @return Code if found, null otherwise.
     */
    @Override
    public Code getCode(long codeID) {
        try {
            return databaseService.executeSelect(GET_CODE, Code.class, codeID);
        } catch (SQLException e) {
            logger.debug("Failed select query. Cause: ", e);
        }

        return null;
    }

    /**
     * Add a code.
     *
     * @param code The code to add.
     * @return Code if successfully added, null otherwise.
     */
    @Override
    public Code addCode(Code code) {
        try {
            databaseService.executeUpdate(ADD_CODE, code);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

        return code;
    }

    /**
     * Update a code.
     *
     * @param code The code to update.
     * @return Code object with the update data if found, null otherwise.
     */
    public Code updateCode(Code code) {
        try {
            databaseService.executeUpdate(UPDATE_CODE, code.getCode(), code.getId());
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

        return code;
    }

    /**
     * Remove a code.
     *
     * @param code Code to remove.
     * @return Code if deleted, null otherwise.
     */
    @Override
    public Code removeCode(Code code) {
        try {
            databaseService.executeUpdate(DELETE_CODE, code.getId());
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

        return code;
    }

    /**
     * Remove all codes for an account.
     *
     * @param account The account to delete the codes for.
     * @return Codes that were deleted, null otherwise.
     */
    @Override
    public List<Code> clearAllCodes(Account account) {
        try {
            databaseService.executeUpdate(DELETE_CODES, account.getID());
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
            return null;
        }

        return List.of(new Code());
    }
}
