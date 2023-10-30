package cypher.enforcers.data.implementations;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.spis.CodeDAO;
import cypher.enforcers.data.spis.CodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/*
 Implementation for the code Repository. Behaves as a collection of
 accounts to help ease on making any changes.
 */
public class CodeRepositoryImpl implements CodeRepository {

    // Logger for the code repository.
    private static final Logger logger = LoggerFactory.getLogger(CodeRepositoryImpl.class);

    // To make updates to the database.
    private final CodeDAO codeDAO;

    /**
     * Create a new Code Repository linked to a Code Data Access Object
     * to work with the codes as a collection.
     *
     * @param dao The Code Data Access Object.
     */
    public CodeRepositoryImpl(CodeDAO dao) {
        this.codeDAO = dao;
    }

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
     * @param id ID of the Account to retrieve the codes for.
     * @return List of codes. Empty list is returned if no codes
     * are present.
     */
    @Override
    public List<Code> readAll(long id) {
        logger.trace("Attempting to get all codes for account with ID {}.", id);

        List<Code> result = codeDAO.getCodes(id);

        if (!Objects.isNull(result)) {
            logger.trace("Accounts retrieved for account with ID {}.", id);
            return result;
        }

        logger.warn("No accounts found for account with ID {}.", id);
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
     * @param id ID of the code to delete.
     * @return An Optional containing the code if deleted, null otherwise.
     */
    @Override
    public Optional<Code> delete(long id) {
        logger.trace("Attempting to delete code with ID {}.", id);

        Code deletedCode = codeDAO.removeCode(id);

        if (!Objects.isNull(deletedCode)) {
            logger.trace("Deleted code successfully.");
            return Optional.of(deletedCode);
        }

        logger.warn("Failed to delete code with ID {}.", id);
        return Optional.empty();
    }

    /**
     * Delete all codes for an account.
     *
     * @param id ID of the account to delete the codes for.
     * @return List containing the codes if successfully deleted,
     * empty list otherwise.
     */
    @Override
    public List<Code> deleteAll(long id) {
        logger.trace("Attempting to delete all codes for account with ID {}.", id);

        List<Code> codes = codeDAO.clearAllCodes(id);

        if (!Objects.isNull(codes)) {
            logger.trace("Deleted all codes successfully.");
            return codes;
        }

        logger.warn("Failed to delete all codes for account with ID {}.", id);
        return Collections.emptyList();
    }
}
