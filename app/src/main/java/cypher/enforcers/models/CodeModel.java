package cypher.enforcers.models;

import cypher.enforcers.code.Code;
import cypher.enforcers.data.security.AccountDTO;
import cypher.enforcers.data.security.CodeDTO;
import cypher.enforcers.data.security.CodeDTOMapper;
import cypher.enforcers.data.spis.CodeRepository;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is used to model a code in our application.
 */
public class CodeModel {

    // Used to interact with the codes objects.
    private final CodeRepository codeRepository;

    private final CodeDTOMapper mapper;

    /**
     * Create a new code model linked to a Code Repository with a mapper
     * to convert a code object to a transfer object.
     *
     * @param repository The repository containing the codes.
     * @param mapper The mapper that converts a code object to be
     *               transferred.
     */
    public CodeModel(CodeRepository repository, CodeDTOMapper mapper) {
        this.codeRepository = repository;
        this.mapper = mapper;
    }

    // list of codes for an account.
    private final ObservableList<CodeDTO> codes = FXCollections.observableArrayList();

    // Property to store the list of codes.
    private final ObjectProperty<ObservableList<CodeDTO>> codesProperty = new SimpleObjectProperty<>(codes);

    /**
     * Get the codes for the current account.
     *
     * @return An ObservableList of codes.
     */
    public ObservableList<CodeDTO> getCodes() {
        return codesProperty.get();
    }

    /**
     * Get the list of codes property.
     *
     * @return Property with an ObservableList of codes.
     */
    public ObjectProperty<ObservableList<CodeDTO>> codesProperty() {
        return codesProperty;
    }

    /**
     * Set the list of codes.
     *
     * @param codes The codes to be set.
     */
    public void setCodes(ObservableList<CodeDTO> codes) {
        codesProperty.set(codes);
    }

    // Property to store the current code.
    private final ObjectProperty<CodeDTO> currentCodeProperty = new SimpleObjectProperty<>();

    /**
     * Get the current code being selected.
     *
     * @return The code being selected.
     */
    public CodeDTO getCurrentCode() {
        return currentCodeProperty.get();
    }

    /**
     * Get the property of the current code.
     *
     * @return Property of the current code.
     */
    public ObjectProperty<CodeDTO> currentCodeProperty() {
        return currentCodeProperty;
    }

    /**
     * Set the current code being selected.
     *
     * @param code The code being selected.
     */
    public void setCurrentCode(CodeDTO code) {
        currentCodeProperty.set(code);
    }

    /**
     * Delete all codes for an account.
     *
     * @param account The account to delete the codes for.
     * @return True if the codes were deleted, false otherwise.
     */
    public boolean deleteAllCodes(AccountDTO account) {
        List<Code> results = codeRepository.deleteAll(account.id());
        if (results.size() == codes.size()) {
            codes.clear();
            return true;
        }

        return false;
    }

    /**
     * Add a new code to an account.
     *
     * @param account The account to add the code for.
     * @param code The code as a string.
     * @return True if code was added, false otherwise.
     */
    public boolean addCode(AccountDTO account, String code) {
        Code c = new Code();
        c.setCode(code);
        c.setAccountID(account.id());

        Optional<Code> optionalCode = codeRepository.create(c);
        if (optionalCode.isPresent()) {
            codes.add(mapper.apply(optionalCode.get()));
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
        CodeDTO code = getCurrentCode();

        if (code == null) {
            return false;
        }

        Optional<Code> optionalCode = codeRepository.delete(code.id());
        if (optionalCode.isPresent() && optionalCode.get().getId() == code.id()) {
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
        CodeDTO code = getCurrentCode();
        if (Objects.isNull(code)) {
            return false;
        }

        int index = codes.indexOf(code);
        if (index == -1) {
            return false;
        }

        if (code.code().equals(newCode)) {
            return true;
        }

        Code c = new Code();
        c.setId(code.id());
        c.setCode(newCode);
        Optional<Code> optionalCode = codeRepository.update(c);
        if (optionalCode.isPresent() && optionalCode.get().getCode().equals(newCode)) {
            codes.set(index, mapper.apply(optionalCode.get()));
            return true;
        }

        return false;
    }
}
