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
     * @param code The code to add.
     * @return True if the code was added, otherwise false.
     */
    boolean create(Code code);

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
     * Update a code.
     *
     * @param code The code to update.
     * @return True if successfully updated, false otherwise.
     */
    boolean update(Code code);

    /**
     * Delete a code.
     *
     * @param code The code to delete.
     * @return True if the code was deleted successfully, false otherwise.
     */
    boolean delete(Code code);

    /**
     * Delete all codes.
     *
     * @return True if all codes were deleted successfully, false
     * otherwise.
     */
    boolean deleteAll(long accountID);

}
