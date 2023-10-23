package cypher.enforcers.data.spis;

import cypher.enforcers.code.Code;
import cypher.enforcers.models.Account;

import java.util.List;
import java.util.Optional;

/*
 Interface for the code Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public interface CodeRepository {

    /**
     * Create a new code.
     *
     * @param accountID ID of the account to which this code should be
     *                  added to.
     * @param code The code to add.
     * @return ID of the code, otherwise -1.
     */
    long create(long accountID, String code);

    /**
     * Read all codes for an account.
     *
     * @param account Account to retrieve the codes for.
     * @return List of codes. Empty list is returned if no codes
     * are present.
     */
    List<Code> readAll(Account account);

    /**
     * Read a code by its ID.
     *
     * @param codeID ID of the code.
     * @return An Optional containing the code. Null otherwise.
     */
    Optional<Code> read(long codeID);

    /**
     * Delete a code.
     *
     * @param codeID ID of code to delete.
     * @return True if the code was deleted successfully, false otherwise.
     */
    boolean delete(long codeID);

    /**
     * Delete all codes.
     *
     * @return True if all codes were deleted successfully, false
     * otherwise.
     */
    boolean deleteAll(long accountID);

}
