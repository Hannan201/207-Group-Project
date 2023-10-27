package cypher.enforcers.data.implementations;

import cypher.enforcers.annotations.SimpleService;
import cypher.enforcers.code.Code;
import cypher.enforcers.data.spis.DatabaseService;
import cypher.enforcers.data.spis.CodeDAO;

import java.util.List;
import java.util.Optional;

/*
 Implementation for the Code Data Access Object (DAO) to communicate to the
 database and make changes to any information related to the accounts.
 */
public class CodeDAOImpl implements CodeDAO {

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
     * @param code The code as a string.
     * @return True if the code was added, false otherwise.
     */
    @Override
    public boolean addCode(long accountID, String code) {
        System.out.println("Adding code for user");
        return false;
    }

    /**
     * Remove a code.
     *
     * @param codeID ID of the code.
     * @return True if code was removed successfully, false otherwise.
     */
    @Override
    public boolean removeCode(long codeID) {
        System.out.println("Removing code for user");
        return false;
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
        System.out.println("Clearing all codes for user");
        return false;
    }
}
