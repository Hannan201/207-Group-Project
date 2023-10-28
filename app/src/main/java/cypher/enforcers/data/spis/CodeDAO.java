package cypher.enforcers.data.spis;

import cypher.enforcers.code.Code;
import cypher.enforcers.models.Account;

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
     * @param account The account to retrieve the codes for.
     * @return List of codes. Returns null if any errors occur.
     */
    List<Code> getCodes(Account account);

    /**
     * Get a code by ID.
     *
     * @param codeID ID of the code to retrieve.
     * @return Code if found, null otherwise.
     */
    Code getCode(long codeID);

    /**
     * Add a code.
     *
     * @param code The code to add.
     * @return Code if successfully added, null otherwise.
     */
    Code addCode(Code code);

    /**
     * Update a code.
     *
     * @param code The code to update.
     * @return Code object with the update data if found, null otherwise.
     */
    Code updateCode(Code code);

    /**
     * Remove a code.
     *
     * @param code Code to remove.
     * @return Code if deleted, null otherwise.
     */
    Code removeCode(Code code);

    /**
     * Remove all codes for an account.
     *
     * @param account The account to delete the codes for.
     * @return Codes that were deleted, null otherwise.
     */
    List<Code> clearAllCodes(Account account);

}
