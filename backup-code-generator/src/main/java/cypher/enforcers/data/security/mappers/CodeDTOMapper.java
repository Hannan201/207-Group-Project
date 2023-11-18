package cypher.enforcers.data.security.mappers;

import cypher.enforcers.data.entities.CodeEntity;
import cypher.enforcers.data.security.dtos.Code;

import java.util.function.Function;

/**
 * Converts a code object to a code data-transfer-object.
 */
public class CodeDTOMapper implements Function<CodeEntity, Code> {

    /**
     * Create a data transfer mapper for the code entity.
     * <br>
     * Mainly here to avoid warnings.
     */
    public CodeDTOMapper() {

    }

    @Override
    public Code apply(CodeEntity code) {
        return new Code(code.getId(), code.getCode());
    }
}
