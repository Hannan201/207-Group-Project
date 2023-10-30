package cypher.enforcers.data.security;

import cypher.enforcers.code.CodeEntity;

import java.util.function.Function;

/**
 * Converts a code object to a code data-transfer-object.
 */
public class CodeDTOMapper implements Function<CodeEntity, CodeDTO> {
    @Override
    public CodeDTO apply(CodeEntity code) {
        return new CodeDTO(code.getId(), code.getCode());
    }
}
