package cypher.enforcers.data.spis;

import cypher.enforcers.code.Code;

import java.util.List;
import java.util.Optional;

/*
 Interface for the Code Data Access Object (DAO) to communicate to the
 database and make changes to any information related to the accounts.
 */
public interface CodeDAO {

    /**
     * Get all codes for an account.
     *
     * @param accountID The ID of the account.
     * @return List of codes. Returns an empty list if no codes
     * are found.
     */
    List<Code> getCodes(long accountID);

    /**
     * Get a code by ID.
     *
     * @param codeID ID of the code to retrieve.
     * @return An optional containing the code if found. Null otherwise.
     */
    Optional<Code> getCode(long codeID);

    /**
     * Add a code.
     *
     * @param code The code to add.
     * @return True if the code was added, false otherwise.
     */
    boolean addCode(Code code);

    /**
     * Update a code.
     *
     * @param code The code to update.
     * @return True if update successfully, false otherwise.
     */
    boolean updateCode(Code code);

    /**
     * Remove a code.
     *
     * @param code Code to remove.
     * @return True if code was removed successfully, false otherwise.
     */
    boolean removeCode(Code code);

    /**
     * Remove all codes for an account.
     *
     * @param accountID ID of the account.
     * @return True if the codes was removed successfully,
     * false otherwise.
     */
    boolean clearAllCodes(long accountID);

}
