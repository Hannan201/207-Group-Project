package cypher.enforcers.models;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.spis.CodeRepository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

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
     * Delete all codes.
     *
     * @param accountID The ID of the account to delete the codes for.
     * @return True if the codes were deleted, false otherwise.
     */
    public boolean deleteAllCodes(long accountID) {
        boolean result = codeRepository.deleteAll(accountID);
        if (result) {
            codes.clear();
        }

        return result;
    }

    /**
     * Add a new code to an account.
     *
     * @param accountID ID of the account.
     * @param code The code as a string.
     * @return True if code was added, false otherwise.
     */
    public boolean addCode(long accountID, String code) {
        Code c = new Code(-1, code);
        boolean result = codeRepository.create(c);
        if (result) {
            codes.add(c);
        }

        return result;
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

        boolean result = codeRepository.delete(code);
        if (result) {
            codes.remove(code);
        }

        return result;
    }

    /**
     * Attempt to update the code.
     *
     * @param newCode The new code to update to.
     * @return True if the code was updated, or if the code
     * hasn't changed from the current code. False otherwise.
     */
    public boolean updateCode(String newCode) {
        Code code = Objects.requireNonNull(getCurrentCode());
        if (code.getCode().equals(newCode)) {
            return true;
        }

        // Call setter method.
        return true;
    }
}
