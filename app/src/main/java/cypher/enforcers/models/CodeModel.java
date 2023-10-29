package cypher.enforcers.models;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.spis.CodeRepository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

/**
 * This class is used to model a code in our application.
 */
public class CodeModel {

    // Used to interact with the codes objects.
    private CodeRepository codeRepository;

    // list of codes for an account.
    private final ObservableList<Code> codes = FXCollections.observableArrayList();

    // Property to store the list of codes.
    private final ObjectProperty<ObservableList<Code>> codesProperty = new SimpleObjectProperty<>(codes);

    /**
     * Get the codes for the current account.
     *
     * @return An ObservableList of codes.
     */
    public ObservableList<Code> getCodes() {
        return codesProperty.get();
    }

    /**
     * Get the list of codes property.
     *
     * @return Property with an ObservableList of codes.
     */
    public ObjectProperty<ObservableList<Code>> codesProperty() {
        return codesProperty;
    }

    /**
     * Set the list of codes.
     *
     * @param codes The codes to be set.
     */
    public void setCodes(ObservableList<Code> codes) {
        codesProperty.set(codes);
    }

    // Property to store the current code.
    private final ObjectProperty<Code> currentCodeProperty = new SimpleObjectProperty<>();

    /**
     * Get the current code being selected.
     *
     * @return The code being selected.
     */
    public Code getCurrentCode() {
        return currentCodeProperty.get();
    }

    /**
     * Get the property of the current code.
     *
     * @return Property of the current code.
     */
    public ObjectProperty<Code> currentCodeProperty() {
        return currentCodeProperty;
    }

    /**
     * Set the current code being selected.
     *
     * @param code The code being selected.
     */
    public void setCurrentCode(Code code) {
        currentCodeProperty.set(code);
    }

    /**
     * Delete all codes for an account given the ID.
     *
     * @param account The ID of the account.
     * @return True if the codes were deleted, false otherwise.
     */
    public boolean deleteAllCodes(Account account) {
        List<Code> results = codeRepository.deleteAll(account);
        if (results.size() == codes.size()) {
            codes.clear();
            return true;
        }

        return false;
    }

    /**
     * Add a new code to an account.
     *
     * @param accountID ID of the account.
     * @param code The code as a string.
     * @return True if code was added, false otherwise.
     */
    public boolean addCode(long accountID, String code) {
        Code c = new Code();
        c.setCode(code);
        c.setAccountID(accountID);

        Optional<Code> optionalCode = codeRepository.create(c);
        if (optionalCode.isPresent()) {
            codes.add(c);
            return true;
        }

        return false;
    }

    /**
     * Attempt to delete the current selected code.
     *
     * @return True if deleted, false otherwise.
     */
    public boolean deleteCode() {
        Code code = getCurrentCode();

        if (code == null) {
            return false;
        }

        Optional<Code> optionalCode = codeRepository.delete(code);
        if (optionalCode.isPresent() && optionalCode.get().getId() == code.getId()) {
            codes.remove(code);
            return true;
        }

        return false;
    }

    /**
     * Attempt to update the code.
     *
     * @param newCode The new code to update to.
     * @return True if the code was updated, or if the code
     * hasn't changed from the current code. False otherwise.
     */
    public boolean updateCode(String newCode) {
        Code code = getCurrentCode();

        if (code == null) {
            return false;
        }

        if (code.getCode().equals(newCode)) {
            return true;
        }

        String previous = code.getCode();

        code.setCode(newCode);
        Optional<Code> optionalCode = codeRepository.update(code);
        if (optionalCode.isPresent() && optionalCode.get().getCode().equals(newCode)) {
            return true;
        }

        code.setCode(previous);
        return false;
    }
}
