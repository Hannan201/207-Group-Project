package cypher.enforcers.data.security;

import cypher.enforcers.data.entities.CodeEntity;

import java.util.function.Function;

/**
 * Converts a code object to a code data-transfer-object.
 */
public class CodeDTOMapper implements Function<CodeEntity, Code> {
    @Override
    public Code apply(CodeEntity code) {
        return new Code(code.getId(), code.getCode());
    }
}
