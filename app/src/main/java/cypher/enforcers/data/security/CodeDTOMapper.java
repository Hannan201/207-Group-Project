package cypher.enforcers.data.security;

import cypher.enforcers.code.Code;

import java.util.function.Function;

/**
 * Converts a code object to a code data-transfer-object.
 */
public class CodeDTOMapper implements Function<Code, CodeDTO> {
    @Override
    public CodeDTO apply(Code code) {
        return new CodeDTO(code.getId(), code.getCode());
    }
}
