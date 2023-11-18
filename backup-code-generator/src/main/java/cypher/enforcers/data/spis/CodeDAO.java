package cypher.enforcers.data.spis;

import cypher.enforcers.data.entities.CodeEntity;

import java.util.List;

/**
 Interface for the Code Data Access Object (DAO) to communicate to the
 database and make changes to any information related to the accounts.
 */
public interface CodeDAO {

    /**
     * Get all codes for an account.
     *
     * @param id ID of the Account to retrieve the codes for.
     * @return List of codes. Returns null if any errors occur.
     */
    List<CodeEntity> getCodes(long id);

    /**
     * Get a code by ID.
     *
     * @param codeID ID of the code to retrieve.
     * @return Code if found, null otherwise.
     */
    CodeEntity getCode(long codeID);

    /**
     * Add a code.
     *
     * @param code The code to add.
     * @return Code if successfully added, null otherwise.
     */
    CodeEntity addCode(CodeEntity code);

    /**
     * Update a code.
     *
     * @param code The code to update.
     * @return Code object with the update data if found, null otherwise.
     */
    CodeEntity updateCode(CodeEntity code);

    /**
     * Remove a code.
     *
     * @param id ID of the code to remove.
     * @return Code if deleted, null otherwise.
     */
    CodeEntity removeCode(long id);

    /**
     * Remove all codes for an account.
     *
     * @param id ID of the account to delete the codes for.
     * @return The codes deleted, null otherwise.
     */
    List<CodeEntity> clearAllCodes(long id);

}
