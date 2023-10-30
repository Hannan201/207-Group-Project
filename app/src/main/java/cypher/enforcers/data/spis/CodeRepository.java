package cypher.enforcers.data.spis;

import cypher.enforcers.code.CodeEntity;

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
    Optional<CodeEntity> create(CodeEntity code);

    /**
     * Read all codes for an account.
     *
     * @param id ID of the Account to retrieve the codes for.
     * @return List of codes. Empty list is returned if no codes
     * are present.
     */
    List<CodeEntity> readAll(long id);

    /**
     * Read a code by its ID.
     *
     * @param codeID ID of the code.
     * @return An Optional containing the code. Null otherwise.
     */
    Optional<CodeEntity> read(long codeID);

    /**
     * Update a code.
     *
     * @param code The code to update.
     * @return An Optional containing the code if updated successfully,
     * null otherwise.
     */
    Optional<CodeEntity> update(CodeEntity code);

    /**
     * Delete a code.
     *
     * @param id ID of the code to delete.
     * @return An Optional containing the code if deleted, null otherwise.
     */
    Optional<CodeEntity> delete(long id);

    /**
     * Delete all codes.
     *
     * @param id ID of the account to delete the codes for.
     * @return List containing the codes if successfully deleted,
     * empty list otherwise.
     */
    List<CodeEntity> deleteAll(long id);

}
