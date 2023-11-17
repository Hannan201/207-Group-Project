package cypher.enforcers.data.security.dtos;

/**
 * Data Transfer Object for the code. This prevents internal details that
 * aren't important from being leaked.
 *
 * @param id   The ID of the code.
 * @param code The code as a string.
 */
public record Code(long id, String code) {
}
