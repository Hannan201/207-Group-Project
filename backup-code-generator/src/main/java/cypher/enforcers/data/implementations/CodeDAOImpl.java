package cypher.enforcers.data.implementations;

import cypher.enforcers.data.entities.CodeEntity;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.CodeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 Implementation for the Code Data Access Object (DAO) to communicate to the
 database and make changes to any information related to the accounts.
 */
public class CodeDAOImpl implements CodeDAO {

    /** SQLite query to insert a code into the database. */
    private static final String ADD_CODE = "INSERT INTO codes (account_id, code) VALUES (?, ?)";

    /**
     * SQLite query to select the most recent code that was inserted
     * into the database.
     */
    private static final String GET_CODE_AFTER_ADDING = "SELECT * FROM codes WHERE id = last_insert_rowid()";

    /** SQLite query to delete all codes from the database. */
    private static final String DELETE_CODES = "DELETE FROM codes WHERE account_id = ?";

    /** SQLite query to delete a code by ID from the database. */
    private static final String DELETE_CODE = "DELETE FROM codes WHERE id = ?";

    /** SQLite query to update a code from the database. */
    private static final String UPDATE_CODE = "UPDATE codes SET code = ? WHERE id = ?";

    /** SQLite query to select all codes by ID from the database. */
    private static final String GET_CODES = "SELECT * FROM codes WHERE account_id = ?";

    /** SQLite query to select a code by ID from the database. */
    private static final String GET_CODE = "SELECT * FROM codes WHERE id = ?";

    /** Logger for the code data access object. */
    private static final Logger logger = LoggerFactory.getLogger(CodeDAOImpl.class);

    /** Service to communicate to the database. */
    private final DatabaseService databaseService;

    /**
     * Create a new Code Data Access Object to load codes
     * from a database.
     *
     * @param service The service that provides a connection to the
     *                database.
     */
    public CodeDAOImpl(DatabaseService service) {
        this.databaseService = service;
    }

    /**
     * Get all codes for an account.
     *
     * @param id ID of the Account to retrieve the codes for.
     * @return List of codes. Returns null if any errors occur.
     */
    @Override
    public List<CodeEntity> getCodes(long id) {
        try {
            return databaseService.executeMultiSelect(GET_CODES, CodeEntity.class, id);
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
    public CodeEntity getCode(long codeID) {
        try {
            return databaseService.executeSelect(GET_CODE, CodeEntity.class, codeID);
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
    public CodeEntity addCode(CodeEntity code) {
        try {
            databaseService.executeUpdate(ADD_CODE, code);

            return databaseService.executeSelect(GET_CODE_AFTER_ADDING, CodeEntity.class);
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
        }

        return null;
    }

    /**
     * Update a code.
     *
     * @param code The code to update.
     * @return Code object with the update data if found, null otherwise.
     */
    public CodeEntity updateCode(CodeEntity code) {
        try {
            databaseService.executeUpdate(UPDATE_CODE, code.getCode(), code.getId());

            return databaseService.executeSelect(GET_CODE, CodeEntity.class, code.getId());
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
        }

        return null;
    }

    /**
     * Remove a code.
     *
     * @param id ID of the code to remove.
     * @return Code if deleted, null otherwise.
     */
    @Override
    public CodeEntity removeCode(long id) {
        try {
            CodeEntity result = getCode(id);

            if (!Objects.isNull(result)) {
                databaseService.executeUpdate(DELETE_CODE, id);
            }

            return result;
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
        }

        return null;
    }

    /**
     * Remove all codes for an account.
     *
     * @param id ID of the account to delete the codes for.
     * @return The codes deleted, null otherwise.
     */
    @Override
    public List<CodeEntity> clearAllCodes(long id) {
        try {
            List<CodeEntity> codes = getCodes(id);

            if (!Objects.isNull(codes)) {
                databaseService.executeUpdate(DELETE_CODES, id);
            }

            return codes;
        } catch (SQLException e) {
            logger.debug("Failed update query. Cause: ", e);
        }

        return null;
    }
}
