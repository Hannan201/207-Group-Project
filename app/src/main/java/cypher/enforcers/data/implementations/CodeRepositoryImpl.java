package cypher.enforcers.data.implementations;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.spis.CodeDAO;
import cypher.enforcers.data.spis.CodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cypher.enforcers.models.Account;
import cypher.enforcers.annotations.SimpleService;

import java.util.*;

/*
 Implementation for the code Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public class CodeRepositoryImpl implements CodeRepository {

    // Logger for the code repository.
    private static final Logger logger = LoggerFactory.getLogger(CodeRepositoryImpl.class);

    // To make updates to the database.
    @SimpleService
    private CodeDAO codeDAO;

    /**
     * Create a new code.
     *
     * @param code The code to add.
     * @return An Optional containing the code if created successfully,
     * null otherwise.
     */
    @Override
    public Optional<Code> create(Code code) {
        logger.trace("Attempting to create code {} for account with ID {}.", code.getCode(), code.getAccountID());

        Code createCode = codeDAO.addCode(code);

        if (!Objects.isNull(createCode)) {
            logger.trace("Code {} created with ID {} for account with ID {}", createCode.getCode(), createCode.getId(), createCode.getAccountID());
            return Optional.of(createCode);
        }

        logger.warn("Unable to create code {} for account with ID {}.", code.getCode(), code.getAccountID());
        return Optional.empty();
    }

    /**
     * Read all codes for an account.
     *
     * @param account Account to retrieve the codes for.
     * @return List of codes. Empty list is returned if no codes
     * are present.
     */
    @Override
    public List<Code> readAll(Account account) {
        logger.trace("Attempting to get all codes for account with ID {}.", account.getID());

        List<Code> result = codeDAO.getCodes(account);

        if (!Objects.isNull(result)) {
            logger.trace("Accounts retrieved for account with ID {}.", account.getID());
            return result;
        }

        logger.warn("No accounts found for account with ID {}.", account.getID());
        return Collections.emptyList();
    }

    /**
     * Read a code by its ID.
     *
     * @param codeID ID of the code.
     * @return An Optional containing the code. Null otherwise.
     */
    @Override
    public Optional<Code> read(long codeID) {
        logger.trace("Attempting to get code with ID {}.", codeID);

        Code code = codeDAO.getCode(codeID);

        if (!Objects.isNull(code)) {
            logger.trace("Code found.");
            return Optional.of(code);
        }

        logger.warn("Failed to get code with ID {}.", codeID);
        return Optional.empty();
    }

    /**
     * Update a code.
     *
     * @param code The code to update.
     * @return An Optional containing the code if updated successfully,
     * null otherwise.
     */
    @Override
    public Optional<Code> update(Code code) {
        logger.trace("Attempting to update code with ID {} to {}.", code.getId(), code.getCode());

        Code updatedCode = codeDAO.updateCode(code);

        if (!Objects.isNull(updatedCode)) {
            logger.trace("Updated code successfully.");
            return Optional.of(updatedCode);
        }

        logger.warn("Failed to update code with ID {} to {}.", code.getId(), code.getCode());
        return Optional.empty();
    }

    /**
     * Delete a code.
     *
     * @param code The code to delete.
     * @return An Optional containing the code if deleted, null otherwise.
     */
    @Override
    public Optional<Code> delete(Code code) {
        logger.trace("Attempting to delete code with ID {}.", code.getId());

        Code deletedCode = codeDAO.removeCode(code);

        if (!Objects.isNull(deletedCode)) {
            logger.trace("Deleted code successfully.");
            return Optional.of(deletedCode);
        }

        logger.warn("Failed to delete code with ID {}.", code.getId());
        return Optional.empty();
    }

    /**
     * Delete all codes for an account.
     *
     * @param account The accounts to delete the codes for.
     * @return List containing the codes if successfully deleted,
     * empty list otherwise.
     */
    @Override
    public List<Code> deleteAll(Account account) {
        logger.trace("Attempting to delete all codes for account with ID {}.", account.getID());

        List<Code> codes = codeDAO.clearAllCodes(account);

        if (!Objects.isNull(codes)) {
            logger.trace("Deleted all codes successfully.");
            return codes;
        }

        logger.warn("Failed to delete all codes for account with ID {}.", account.getID());
        return Collections.emptyList();
    }
}
