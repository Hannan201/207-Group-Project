package cypher.enforcers.models;

import cypher.enforcers.behaviors.interfaces.ReadCodeBehavior;
import cypher.enforcers.data.entities.CodeEntity;
import cypher.enforcers.data.security.dtos.Account;
import cypher.enforcers.data.security.dtos.Code;
import cypher.enforcers.data.security.mappers.CodeDTOMapper;
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

    // Converts entity to data transfer object.
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
     * Load codes for an account given the ID.
     *
     * @param id The ID of the account.
     */
    public void loadCodes(long id) {
        List<Code> converted = codeRepository.readAll(id)
                .stream()
                .map(mapper)
                .toList();

        codes.setAll(converted);
    }

    /**
     * Delete all codes for an account.
     *
     * @param account The account to delete the codes for.
     */
    public void deleteAllCodes(Account account) {
        List<CodeEntity> results = codeRepository.deleteAll(account.id());
        if (results.size() == codes.size()) {
            codes.clear();
        }
    }
    /**
     * Add a list of codes for an account based on the behavior object
     * which determines the source.
     *
     * @param account The account to add the codes for.
     * @param source The behavior object which contains the source.
     */
    public void addCodes(Account account, ReadCodeBehavior source) {
        List<String> returned = source.readCodes();
        for (String s : returned) {
            if (!s.isEmpty()) {
                CodeEntity c = new CodeEntity();
                c.setCode(s);
                c.setAccountID(account.id());

                Optional<CodeEntity> optionalCode = codeRepository.create(c);
                if (optionalCode.isEmpty()) {
                    return;
                }

                codes.add(mapper.apply(optionalCode.get()));
            }
        }
    }

    /**
     * Attempt to delete the current selected code.
     */
    public void deleteCode() {
        Code code = getCurrentCode();

        if (!Objects.isNull(code)) {
            Optional<CodeEntity> optionalCode = codeRepository.delete(code.id());
            if (optionalCode.isPresent() && optionalCode.get().getId() == code.id()) {
                codes.remove(code);
            }
        }
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
        if (!Objects.isNull(code)) {
            if (code.code().equals(newCode)) {
                return true;
            }

            int index = codes.indexOf(code);
            if (index == -1) {
                return false;
            }

            CodeEntity c = new CodeEntity();
            c.setId(code.id());
            c.setCode(newCode);
            Optional<CodeEntity> optionalCode = codeRepository.update(c);
            if (optionalCode.isPresent() && optionalCode.get().getCode().equals(newCode)) {
                codes.set(index, mapper.apply(optionalCode.get()));
                return true;
            }
        }

        return false;
    }

    /**
     * Clear the current list of codes. usually called when no
     * account is selected and the code view is loaded.
     */
    public void clear() {
        codes.clear();
    }
}
