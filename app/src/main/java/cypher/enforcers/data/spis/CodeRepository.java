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
     * @return An Optional containing the code if created successfully,
     * null otherwise.
     */
    Optional<Code> create(Code code);

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
     * @return An Optional containing the code if updated successfully,
     * null otherwise.
     */
    Optional<Code> update(Code code);

    /**
     * Delete a code.
     *
     * @param code The code to delete.
     * @return An Optional containing the code if deleted, null otherwise.
     */
    Optional<Code> delete(Code code);

    /**
     * Delete all codes.
     *
     * @param account The accounts to delete the codes for.
     * @return List containing the codes if successfully deleted,
     * empty list otherwise.
     */
    List<Code> deleteAll(Account account);

}
